package com.piesat.kettlescheduler.controller;

import com.piesat.kettlescheduler.common.Constant;
import com.piesat.kettlescheduler.common.Result;
import com.piesat.kettlescheduler.common.ResultCode;
import com.piesat.kettlescheduler.model.KUser;
import com.piesat.kettlescheduler.service.UserService;
import com.piesat.kettlescheduler.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/user/")
@Api(value = "UserController|用户管理接口")
public class UserController {

	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "getList.shtml",method= RequestMethod.GET)
	@ApiOperation(value="获取用户分页列表", notes="获取成功或失败")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType="query", name = "offset", value = "行数", required = true, dataType = "Integer"),
			@ApiImplicitParam(paramType="query", name = "limit", value = "每页数量", required = true, dataType = "Integer")
	})
	public String getList(Integer offset, Integer limit){
		return Result.success(userService.getList(offset, limit),ResultCode.SUCCESS);
	}
	
	@RequestMapping(value = "delete.shtml",method= RequestMethod.DELETE)
	@ApiOperation(value="根据用户ID删除用户", notes="删除成功或失败")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType="query", name = "uId", value = "用户ID", required = true, dataType = "Integer")
	})
	public String delete(Integer uId){
		userService.delete(uId);
		return Result.fail(ResultCode.DELETE_SUCCESS);
	}
	
	@RequestMapping(value = "resetPassword.shtml", method= RequestMethod.GET)
	@ApiOperation(value="重置用户名密码", notes="重置成功或失败")
	public String resetPassword(){
		
		return Result.success();
	}
	@RequestMapping(value = "insert.shtml",method= RequestMethod.POST)
	@ApiOperation(value="插入用户信息", notes="插入成功或失败")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType="query", name = "uNickname", value = "用户昵称", required = true, dataType = "String"),
			@ApiImplicitParam(paramType="query", name = "uEmail", value = "用户邮箱", required = true, dataType = "String"),
			@ApiImplicitParam(paramType="query", name = "uPhone", value = "用户电话", required = true, dataType = "String"),
			@ApiImplicitParam(paramType="query", name = "uAccount", value = "用户账号", required = true, dataType = "String"),
			@ApiImplicitParam(paramType="query", name = "uPassword", value = "用户密码", required = true, dataType = "String")
	})
	public String insert(KUser kNewUser, HttpServletRequest request){
		KUser kUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
		userService.insert(kNewUser, kUser.getuId());
		return Result.success(ResultCode.SUCCESS);
	}
	@RequestMapping(value = "IsAccountExist.shtml",method= RequestMethod.GET)
	@ApiOperation(value="判断账号是否存在", notes="存在或不存在")
	public String IsAccountExist(String uAccount, HttpServletResponse response) {
		try {
			if (userService.IsAccountExist(uAccount)) {
				return Result.success(true, ResultCode.USER_EXIST);
			} else {
				return Result.fail(ResultCode.USER_NOT_EXIST);

			}
		} catch (Exception e) {
			return Result.success(e.toString());
		}
	}
	@RequestMapping(value = "getUser.shtml",method= RequestMethod.GET)
	@ApiOperation(value="根据用户ID获取该用户信息", notes="获取成功或失败")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType="query", name = "uId", value = "用户ID", required = true, dataType = "Integer")
	})
	public String getQuartz(Integer uId){
		return Result.success(userService.getUser(uId),ResultCode.SUCCESS);
	}

	@RequestMapping(value = "update.shtml",method= RequestMethod.POST)
	@ApiOperation(value="更新用户信息", notes="更新成功或失败")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType="query", name = "uId", value = "用户ID", required = true, dataType = "Integer"),
			@ApiImplicitParam(paramType="query", name = "uNickname", value = "用户昵称", required = true, dataType = "String"),
			@ApiImplicitParam(paramType="query", name = "uEmail", value = "用户邮箱", required = true, dataType = "String"),
			@ApiImplicitParam(paramType="query", name = "uPhone", value = "用户电话", required = true, dataType = "String"),
			@ApiImplicitParam(paramType="query", name = "uAccount", value = "用户账号", required = true, dataType = "String"),
			@ApiImplicitParam(paramType="query", name = "uPassword", value = "用户密码", required = true, dataType = "String")
	})
	public String update(KUser kUser, HttpServletRequest request){
		KUser kLoginUser = (KUser) request.getSession().getAttribute(Constant.SESSION_ID);
		try{
			userService.update(kUser, kLoginUser.getuId());
			return Result.success(true,ResultCode.SUCCESS);
		}catch(Exception e){
			return Result.fail(ResultCode.UPDATE_FAILURE);
		}
	}
}
