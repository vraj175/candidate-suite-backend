package com.aspire.kgp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

@RestController
@RequestMapping("/api/v1.0")
@Api(tags = {"Galaxy"})
@SwaggerDefinition(tags = {@Tag(name = "Galaxy", description = "Rest API For Galaxy")})
public class GalaxyRestController {

}
