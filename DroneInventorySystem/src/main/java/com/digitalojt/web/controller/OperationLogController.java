package com.digitalojt.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitalojt.web.consts.LogMessage;
import com.digitalojt.web.consts.ModelAttributeContents;
import com.digitalojt.web.consts.UrlConsts;
import com.digitalojt.web.entity.OperationLog;
import com.digitalojt.web.service.OperationLogService;

import lombok.RequiredArgsConstructor;

/**
 * 操作履歴画面のコントローラークラス
 * 
 * @author dotlife
 *
 */
@Controller
@RequiredArgsConstructor
public class OperationLogController extends AbstractController {

	/** 操作履歴 サービス */
	private final OperationLogService operationLogService;

	/**
	 * 初期表示
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping(UrlConsts.OPERATION_LOG)
	public String index(Model model) {

		logStart(LogMessage.HTTP_GET);
		try {
			// 操作履歴情報の取得
			List<OperationLog> operationLogList = operationLogService.getOperationLogList();
			model.addAttribute(ModelAttributeContents.OPERATION_LOG_LIST, operationLogList);
        } catch (Exception e) {
            return "error";
        }
		logEnd(LogMessage.HTTP_GET);
		return UrlConsts.OPERATION_LOG_INDEX;
	}
	
    /**
     * 画面ロード時にAjaxを利用した非同期処理を実行した初期表示
     * 
     * @param model
     * @param page ページ番号
     * @param size 取得件数
     * @return ページングされた操作履歴のリスト
     */
    @GetMapping("/operationLog")
    @ResponseBody
    public List<OperationLog> pagedOperationLog(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logStart(LogMessage.HTTP_GET);
        // 操作履歴情報の取得
        List<OperationLog> operationLogList = operationLogService.getPagedOperationLog(page, size);
        logEnd(LogMessage.HTTP_GET);
        return operationLogList;
    }
}
