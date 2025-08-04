/**
 * 画面ロード時にAjaxを利用した非同期処理を実行し、最初の10件を取得してテーブルに表示
 */
let currentPage = 0;
const itemsPerPage = 10;
let hasMoreData = true;

document.addEventListener("DOMContentLoaded", () => {
    loadPage(); // DOMのロード
});

function loadPage() { // loadPage関数の実行
	if (!hasMoreData) return;

    fetch(`/operationLog?page=${currentPage}&size=${itemsPerPage}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTPエラーが発生しました。: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log("取得データ: ", data);
			
			// データが空ならテーブルに「データなし」のメッセージを表示
			if (data.length === 0 && currentPage === 0) {
			    document.querySelector("#operationLogBody").innerHTML = "<tr><td colspan='5'>データがありません</td></tr>";
				console.warn("データがありません");
			    hasMoreData = false;
			    return;
			} 
			// データ取得
			const tableBody = document.querySelector("#operationLogBody");
			let rows = data.map(item => `
				<tr>
				    <td>${item.adminInfo.adminName}</td>
				    <td>${item.tableKey}</td>
				    <td>${item.operateType}</td>
				    <td>${item.status}</td>
				    <td>${item.createDate}</td>
				</tr>`).join('');
				tableBody.insertAdjacentHTML("beforeend", rows);
				
				currentPage++;
			})
			.catch(error => {
			    console.error("データ取得時にエラーが発生しました。:", error);
			});
}

// スクロール時、次データを取得
document.querySelector(".table-responsive").addEventListener("scroll", function () {
    if (this.scrollTop + this.clientHeight >= this.scrollHeight - 50) {
        loadPage();
    }
});