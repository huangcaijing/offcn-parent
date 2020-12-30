package com.offcn.order.service;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.order.vo.resp.TReturn;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "SCW-PROJECT")
public interface ProjectServiceFeign {
    @GetMapping("/project/details/returns/{projectId}")
    public AppResponse<List<TReturn>> returnInfo(@PathVariable(name="projectId") Integer projectId);
}
