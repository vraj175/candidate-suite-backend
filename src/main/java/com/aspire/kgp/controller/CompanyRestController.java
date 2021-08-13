package com.aspire.kgp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.CompanyDTO;
import com.aspire.kgp.exception.NotFoundException;
import com.aspire.kgp.model.User;
import com.aspire.kgp.service.UserService;
import com.aspire.kgp.util.CompanyUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

@RestController
@RequestMapping("/api/v1.0")
@Api(tags = {"Company"})
@SwaggerDefinition(tags = {@Tag(name = "Company", description = "Rest API For Company")})
public class CompanyRestController {

  static Log log = LogFactory.getLog(CompanyRestController.class.getName());

  @Autowired
  UserService userService;

  @Autowired
  CompanyUtil companyUtil;

  @ApiOperation(value = "get client list")
  @GetMapping("/companies/{stage}")
  public List<CompanyDTO> getCompanyList(HttpServletRequest request,
      @PathVariable("stage") String stage) {
    User user = (User) request.getAttribute("user");
    if (user == null) {
      throw new NotFoundException("Partner Not Found");
    }
    List<CompanyDTO> companyList;
    if (user.getRole() != null && user.getRole().getName().equalsIgnoreCase(Constant.PARTNER)) {
      companyList = companyUtil.getCompanyList(stage);
    } else {
      throw new NotFoundException("Partner Not Found");
    }
    return companyList;
  }

}