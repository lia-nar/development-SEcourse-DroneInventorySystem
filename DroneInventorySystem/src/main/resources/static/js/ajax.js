/**
 * fetch や $.ajax を使わず に XMLHttpRequest を使って非同期でデータを取得し、画面に表示
 */
// **ページネーション用の変数**
let currentPage = 0;
const itemsPerPage = 10;
let hasMoreData = true;

// **ページロード時にデータを取得**
document.addEventListener("DOMContentLoaded", function () {
    console.log("✅ DOM がロードされました");
    loadPage(); // 初回ロード
});

/**
 * 操作履歴データを非同期で取得してテーブルに表示
 */
function loadPage() {
    if (!hasMoreData) return;

    console.log(`データ取得開始: page=${currentPage}, size=${itemsPerPage}`);

    var xhr = new XMLHttpRequest();
    xhr.open("GET", `/operationLog?page=${currentPage}&size=${itemsPerPage}`, true);
    xhr.setRequestHeader("Content-Type", "application/json");

    xhr.onload = function () {
        if (xhr.status === 200) {
            try {
                var data = JSON.parse(xhr.responseText);
                console.log("取得データ:", data);

                // **データの構造をチェック**
                if (!Array.isArray(data)) {
                    console.error("`data` が配列ではありません。データ構造を確認してください。", data);
                    return;
                }

                if (data.length === 0) {
                    console.warn("取得データが空です。");
                    hasMoreData = false;
                    return;
                }

                // **テーブルにデータを追加**
                var tableBody = document.getElementById("operationLogBody");
                var rows = data.map(item => `
                    <tr>
                        <td>${item.adminInfo.adminName}</td>
                        <td>${item.tableKey}</td>
                        <td>${item.operateType}</td>
                        <td>${item.status}</td>
                        <td>${item.createDate}</td>
                    </tr>
                `).join('');
                tableBody.insertAdjacentHTML("beforeend", rows);

                // **次ページのデータ取得準備**
                currentPage++;
            } catch (error) {
                console.error("JSON パースエラー:", error);
            }
        } else {
            console.error(`HTTPエラー: ${xhr.status}`);
        }
    };

    xhr.onerror = function () {
        console.error("ネットワークエラーが発生しました");
    };

    xhr.send();
}

// **スクロール時に次ページを取得**
document.querySelector(".table-responsive").addEventListener("scroll", function () {
    if (this.scrollTop + this.clientHeight >= this.scrollHeight - 50) {
        loadPage();
    }
});
