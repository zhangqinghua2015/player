package com.zqh.player.controller;

import com.google.common.collect.Lists;

import com.zqh.player.tools.common.page.R;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @discription:
 * @date: 2019/09/22 16:09
 */
@RestController()
@RequestMapping("/video")
public class VideoController {

    private static final String VIDEO_ROOT = "static/static/video";

    @GetMapping("/list")
    public R getVideoList(@RequestParam(required = false) String videoTitle) {
        File videoDir = new File(this.getClass().getResource("/").getPath() + VIDEO_ROOT);
        List<String> videoTitles = Lists.newArrayList();
        if (videoDir.exists()) {
            String[] files = videoDir.list();
            for (String file : files) {
                if (StringUtils.isBlank(videoTitle)) {
                    videoTitles.add(file);
                    continue;
                }
                if (file.contains(videoTitle)) {
                    videoTitles.add(file);
                }
            }
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("list", videoTitles);
        return R.data(result);
    }

}
