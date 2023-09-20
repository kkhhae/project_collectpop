$(document).ready(function (){
    const sortSelector1 = $("#sort-selector1");
    const storeWrap = $("#store-wrap");

    function updateStoreList1() {
        const selectedValue1 = sortSelector1.val();
        console.log("selectedValue1 = " + selectedValue1);
        $.ajax({
            url: `/collectpop/store/filter1?status1=${selectedValue1}`,

            method: "GET",
            success: function(data) {
                console.log("data = " + data);
                storeWrap.html(data);
            },
            error: function(error) {
                console.error("Error:", error);
            }
        });
    }

    sortSelector1.on("change", updateStoreList1);
    updateStoreList1()

    const sortSelector2 = $("#sort-selector2");

    function updateStoreList2() {
        const selectedValue2 = sortSelector2.val();

        $.ajax({
            url: `/collectpop/store/filter2?status2=${selectedValue2}`,

            method: "GET",
            success: function(data) {
                storeWrap.html(data); // 서버로부터 받은 데이터로 스토어 리스트 업데이트
            },
            error: function(error) {
                console.error("Error:", error);
            }
        });
    }

    sortSelector2.on("change", updateStoreList2);
    updateStoreList2(); // 초기 로딩 시 스토어 목록 업데이트

    const sortSelector3 = $("#sort-selector3");

    sortSelector3.on("change", function() {
        const selectedValue3 = sortSelector3.val();

        // 선택된 값을 이용하여 서버로 요청을 보내고 결과를 업데이트하는 작업
        $.ajax({
            url: `/collectpop/store/filter3?status3=${selectedValue3}`,
            method: "GET",
            success: function(data) {
                storeWrap.html(data); // 서버로부터 받은 데이터로 스토어 리스트 업데이트
            },
            error: function(error) {
                console.error("Error:", error);
            }
        });
    });

    const searchInput = document.getElementById("search-input");
    const searchButton = document.getElementById("search-button");
    const searchCategory = document.getElementById("search-category");
    const storewrap = document.getElementById("store-wrap");
    function performSearch() {
        const searchText = searchInput.value;
        const selectedCategory = searchCategory.value;

        // 서버로 검색 요청을 보내고 결과를 업데이트하는 작업
        $.ajax({
            url: `/collectpop/store/search?query=${searchText}&category=${selectedCategory}`,
            method: "GET",
            success: function(data) {
                console.log("data = " + data)
                storewrap.innerHTML = data; // 변경된 부분: innerHTML을 사용하여 결과를 표시
            },
            error: function(error) {
                console.error("Error:", error);
            }
        });
    }

    searchButton.addEventListener("click", performSearch);
    searchInput.addEventListener("keydown", function(event) {
        if (event.key === "Enter") {
            performSearch();
        }
    });

    // 검색 카테고리 변경 시 검색어 입력 필드 초기화
    searchCategory.addEventListener("change", function() {
        searchInput.value = "";
    });
})// ready end