package com.twentythree.peech.common.app;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppInfoController {

    @GetMapping("api/v1.1/app")
    public ResponseEntity<AppInfo> getAppInfo() {

        return ResponseEntity.status(200).body(new AppInfo("6", false));
    }
}
