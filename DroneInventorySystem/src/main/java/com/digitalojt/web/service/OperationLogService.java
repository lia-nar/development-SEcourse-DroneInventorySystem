package com.digitalojt.web.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.digitalojt.web.consts.OperationStatus;
import com.digitalojt.web.consts.OperationType;
import com.digitalojt.web.consts.ScreenTitle;
import com.digitalojt.web.entity.OperationLog;
import com.digitalojt.web.repository.OperationLogRepository;

import lombok.RequiredArgsConstructor;

/**
 * 操作履歴画面のサービスクラス
 *
 * @author dotlife
 * 
 */
@Service
@RequiredArgsConstructor
public class OperationLogService {

	/** 操作履歴テーブル リポジトリー */
	private final OperationLogRepository repository;
	
	/**
	 * 操作履歴情報を全件検索で取得
	 * 
     * @param page ページ番号
     * @param size 取得件数
     * @return 操作履歴テーブルから取得した値をデータ加工したデータ
	 */
    public List<OperationLog> getPagedOperationLog(int page, int size) {
        List<OperationLog> allLogs = repository.findAllByOrderByCreateDateDesc();

        // ページング処理
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, allLogs.size());

        if (fromIndex > allLogs.size()) {
            return List.of();
        }
        
        List<OperationLog> convertedList = convertOperationLogList(allLogs);
        return convertedList.subList(fromIndex, toIndex);
    }
    
	/**
	 * 操作履歴情報を全件検索で取得
	 * 
	 * @return
	 */
	public List<OperationLog> getOperationLogList() {

		// 操作履歴情報の取得
		List<OperationLog> operationLogList = repository.findAll();

		// 画面表示用にデータ加工した結果を返却
		return convertOperationLogList(operationLogList);
	}

	/**
	 * 画面表示用にデータ加工
	 * 
	 * @param operationLogList
	 * @return
	 */
	private List<OperationLog> convertOperationLogList(List<OperationLog> operationLogList) {
		return operationLogList.stream()
				.map(log -> {
					String screenName = convertTableKey(log.getTableKey());
					String operateType = convertOperateType(log.getOperateType());
					String operationStatus = convertOperationStatus(log.getStatus());

					log.setTableKey(screenName);
					log.setOperateType(operateType);
					log.setStatus(operationStatus);

					return log;
				})
				.collect(Collectors.toList());
	}

	/**
	 * テーブルキーを画面表示用にデータ加工
	 * 
	 * @param tableKey
	 * @return
	 */
	private String convertTableKey(String tableKey) {
		return ScreenTitle.fromTableKey(tableKey);
	}

	/**
	 * 操作種類を画面表示用にデータ加工
	 * 
	 * @param operateType
	 * @return
	 */
	private String convertOperateType(String operateType) {
		return OperationType.fromTypeCode(operateType);
	}

	/**
	 * 操作ステータスを画面表示用にデータ加工
	 * 
	 * @param operateType
	 * @return
	 */
	private String convertOperationStatus(String operateStatus) {
		return OperationStatus.fromStatusCode(operateStatus);
	}
}
