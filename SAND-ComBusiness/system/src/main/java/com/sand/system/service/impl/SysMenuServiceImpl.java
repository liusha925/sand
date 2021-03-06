/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sand.core.enums.OperateType;
import com.sand.core.exception.BusinessException;
import com.sand.core.util.lang3.StringUtil;
import com.sand.core.util.tree.Tree;
import com.sand.core.util.tree.TreeUtil;
import com.sand.core.util.tree.builder.ITreeBuilder;
import com.sand.core.util.validator.ModelValidator;
import com.sand.core.vo.ResultVO;
import com.sand.system.entity.SysMenu;
import com.sand.system.entity.SysRoleMenu;
import com.sand.system.enums.MenuEnum;
import com.sand.system.mapper.SysMenuMapper;
import com.sand.system.model.SysMenuModel;
import com.sand.system.service.ISysMenuService;
import com.sand.system.service.ISysRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 功能说明：系统菜单
 * 开发人员：@author liusha
 * 开发日期：2019/8/26 16:12
 * 功能描述：系统菜单
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {
  @Autowired
  private ISysRoleMenuService roleMenuService;

  @Override
  public List<SysMenu> getMenuList(boolean needButton) {
    List<SysMenu> menuList;
    if (needButton) {
      menuList = super.list();
    } else {
      menuList = super.list(new QueryWrapper<SysMenu>().ne("menu_type", MenuEnum.Type.F.getValue()));
    }
    return menuList;
  }

  @Override
  public Tree buildLeftTree(boolean needButton) {
    return buildMenuTree(needButton, true, true, new Object[0]);
  }

  @Override
  public Tree buildLeftTree(boolean needButton, Object[] roleIds) {
    return buildMenuTree(needButton, false, true, roleIds);
  }

  @Override
  public Tree buildMenuTree(boolean needButton) {
    return buildMenuTree(needButton, true, false, new Object[0]);
  }

  @Override
  public Tree buildMenuTree(boolean needButton, Object[] roleIds) {
    return buildMenuTree(needButton, false, false, roleIds);
  }

  @Override
  public Tree buildMenuTree(boolean needButton, boolean isAdmin, boolean isLeft, Object[] roleIds) {
    List<SysMenu> menuList = this.getMenuList(needButton);
    // 查询角色已经拥有的菜单
    List<Object> menuIds = isAdmin
        ? super.listObjs(new QueryWrapper<SysMenu>().select("menu_id"))
        : roleMenuService.listObjs(new QueryWrapper<SysRoleMenu>().select("menu_id").in("role_id", roleIds));
    // 过滤掉重复的菜单ID
    menuIds = new ArrayList<>(menuIds.stream().collect(Collectors.groupingBy(Object::toString, Collectors.toList())).keySet());
    // 筛选出需要的菜单
    if (isLeft) {
      List<SysMenu> tempMenuList = new ArrayList<>();
      List<Object> tempMenuIds = menuIds;
      menuList.forEach(menu ->
          tempMenuIds.forEach(menuId -> {
            if (Objects.equals(menuId.toString(), menu.getMenuId())) {
              tempMenuList.add(menu);
            }
          })
      );
      menuList = tempMenuList;
    }
    // 构建菜单树
    return buildTree(menuList, menuIds);
  }

  @Override
  @Transactional(rollbackFor = BusinessException.class)
  public int add(SysMenuModel model) {
    // 参数校验
    checkModel(model, OperateType.INSERT);
    // 信息入库
    if (!super.save(model)) {
      throw new BusinessException("新增菜单信息入库异常！");
    }
    return 0;
  }

  @Override
  @Transactional(rollbackFor = BusinessException.class)
  public int edit(SysMenuModel model) {
    // 参数校验
    checkModel(model, OperateType.UPDATE);
    // 信息入库
    if (!super.updateById(model)) {
      throw new BusinessException("修改菜单信息入库异常！");
    }
    return 0;
  }

  /**
   * 构建树
   *
   * @param menuList 菜单集合
   * @param viewIds  已有菜单
   * @return
   */
  private Tree buildTree(List<SysMenu> menuList, List<Object> viewIds) {
    return new Tree().buildTree(menuList, viewIds, new ITreeBuilder<SysMenu>() {
      @Override
      public String getId(SysMenu menu) {
        return menu.getMenuId();
      }

      @Override
      public String getPid(SysMenu menu) {
        return menu.getParentId();
      }

      @Override
      public String getName(SysMenu menu) {
        return menu.getMenuName();
      }
    });
  }

  /**
   * 参数校验
   *
   * @param model dto
   */
  private void checkModel(SysMenuModel model, OperateType operate) {
    // 表单注解校验，非空，长度，正则等校验
    ModelValidator.checkModel(model);
    // 不为主目录时需要校验父级菜单
    if (!Objects.equals(model.getParentId(), TreeUtil.TREE_ROOT)) {
      SysMenu parentMenu = super.getById(model.getParentId());
      if (Objects.isNull(parentMenu)) {
        throw new BusinessException(ResultVO.Code.PARAM_CHECKED_ERROR, "父级菜单不存在！");
      }
    }
    // 菜单名查询条件组装
    QueryWrapper<SysMenu> menuNameWrapper = new QueryWrapper<>();
    menuNameWrapper.eq("parent_id", model.getParentId()).eq("menu_name", model.getMenuName());
    if (Objects.equals(operate, OperateType.INSERT)) {
      if (StringUtil.isNotBlank(model.getMenuId())) {
        SysMenu dbMenu = super.getById(model.getMenuId());
        if (Objects.nonNull(dbMenu)) {
          throw new BusinessException(ResultVO.Code.PARAM_CHECKED_ERROR, "此菜单信息已存在！");
        }
      }
    } else if (Objects.equals(operate, OperateType.UPDATE)) {
      if (StringUtil.isBlank(model.getMenuId())) {
        throw new BusinessException(ResultVO.Code.PARAM_MISSING_ERROR, "菜单ID不能为空！");
      }
      SysMenu dbMenu = super.getById(model.getMenuId());
      if (Objects.isNull(dbMenu)) {
        throw new BusinessException(ResultVO.Code.PARAM_CHECKED_ERROR, "此菜单信息不存在！");
      }
      menuNameWrapper.ne("menu_id", model.getMenuId());
    }
    // 校验同级目录下菜单名是否重复
    List<SysMenu> menuNameList = super.list(menuNameWrapper);
    if (menuNameList.size() > 0) {
      throw new BusinessException(ResultVO.Code.PARAM_CHECKED_ERROR, "此菜单名称已存在！");
    }
  }

}
