package net.kuper.tz.active.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.kuper.tz.active.service.EnvService;
import net.kuper.tz.core.controller.Res;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api(value = "监控信息", description = "监控信息")
@RestController
@RequestMapping("/active/env")
public class EnvController {

    @Autowired
    private EnvService envService;

    /**
     * 项目信息
     *
     * @return
     */
    @ApiOperation("项目信息")
    @RequiresPermissions("monitor:env:app")
    @ResponseBody
    @GetMapping("/application")
    public Res<Map<String, Object>> application() {
        return Res.ok(envService.application());
    }

    /**
     * 操作系统
     *
     * @return
     */
    @ApiOperation("操作系统")
    @RequiresPermissions("monitor:env:os")
    @ResponseBody
    @GetMapping("/os")
    public Res<Map<String, Object>> os() {
        return Res.ok(envService.os());
    }

    /**
     * 虚拟机内存
     *
     * @return
     */
    @ApiOperation("虚拟机内存")
    @RequiresPermissions("monitor:env:java")
    @ResponseBody
    @GetMapping("/vmmemory")
    public Res<Map<String, Object>> vmMemory() {
        return Res.ok(envService.vmMemory());
    }

    /**
     * 内存信息
     *
     * @return
     */
    @ApiOperation("内存信息")
    @RequiresPermissions("monitor:env:memory")
    @ResponseBody
    @GetMapping("/memory")
    public Res<Map<String, Object>> memory() {
        return Res.ok(envService.memory());
    }

    /**
     * CPU信息
     *
     * @return
     */
    @ApiOperation("CPU信息")
    @RequiresPermissions("monitor:env:cpu")
    @ResponseBody
    @GetMapping("/cpus")
    public Res<List<Map<String, Object>>> cpus() {
        return Res.ok(envService.cpu());
    }

    /**
     * 磁盘信息
     *
     * @return
     */
    @ApiOperation("磁盘信息")
    @RequiresPermissions("monitor:env:disk")
    @ResponseBody
    @GetMapping("/disks")
    public Res<List<Map<String, Object>>> disk() {
        return Res.ok(envService.disk());
    }

    /**
     * 网络信息
     *
     * @return
     */
    @ApiOperation("网络传输信息")
    @RequiresPermissions("monitor:env:net")
    @ResponseBody
    @GetMapping("/nets")
    public Res<List<Map<String, Object>>> net() {
        return Res.ok(envService.net());
    }

    /**
     * 网卡信息
     *
     * @return
     */
    @ApiOperation("以太网信息")
    @RequiresPermissions("monitor:env:ethernet")
    @ResponseBody
    @GetMapping("/ethernets")
    public Res<List<Map<String, Object>>> ethernet() {
        return Res.ok(envService.ethernet());
    }
}
