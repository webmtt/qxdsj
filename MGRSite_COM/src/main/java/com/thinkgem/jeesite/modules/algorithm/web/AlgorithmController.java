package com.thinkgem.jeesite.modules.algorithm.web;

import com.thinkgem.jeesite.common.web.BaseController;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 描述：算法在线应用信息控制类
 *
 * @author yang.kq
 * @version 1.0 2019年10月30日
 */
@Controller
@RequestMapping(value = "algorithm/meetingarrange")
public class AlgorithmController extends BaseController {
    private static Logger logger = Logger.getLogger(AlgorithmController.class);

}
