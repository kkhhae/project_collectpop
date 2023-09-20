$(document).ready(function (){
    const printme = $("#printMe");
    printHTML()
    $(".plusBtn").click(function () {
        Swal.fire({
            title: '내용을 작성해주세요.',
            input: 'text',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '승인',
            cancelButtonText: '취소',
            preConfirm: (text) => {
                if (!text.trim()) { // 빈 문자열인 경우
                    Swal.showValidationMessage("내용을 입력해주세요.");
                    return false; // 입력이 유효하지 않으므로 false 반환
                }
                return $.ajax({
                    url: "/collectpop/admin/insertEx",
                    type: "post",
                    data: { text: text }, // 입력된 텍스트 데이터를 서버로 보냄
                    success: function (result) {
                        return result;
                    },
                    error: function (e) {
                        Swal.showValidationMessage("요청실패..");
                    },
                    complete: function (e){
                        printHTML()
                    }
                });
            },
            allowOutsideClick: () => !Swal.isLoading(),
        }).then((result) => {
            if (result.isConfirmed) {
                if (result.value) {
                    Swal.fire({
                        title: `${result.value}.`,
                        icon: "success",
                        confirmButtonText: '닫기'
                    });
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: '에러',
                        text: '저장중 오류가 발생했습니다.',
                        confirmButtonText: '닫기'
                    });
                }
            }
        });
    });
    $(document).on("click", ".deleteBtn", function() {
        let dleid = $(this).data("dleid");
        Swal.fire({
            icon: "warning",
            title: '정말 삭제 하시겠습니까?',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '삭제',
            cancelButtonText: '취소',
            preConfirm: () => {
                return $.ajax({
                    url: "/collectpop/admin/deletedecex",
                    type: "post",
                    data: { dleid: dleid }, // 입력된 텍스트 데이터를 서버로 보냄
                    success: function (result) {
                        return result;
                    },
                    error: function (e) {
                        Swal.showValidationMessage("요청실패..");
                    },
                    complete: function (e){
                        printHTML();
                    }
                });
            },
            allowOutsideClick: () => !Swal.isLoading(),
        }).then((result) => {
            if (result.isConfirmed) {
                if (result.value) {
                    Swal.fire({
                        title: `${result.value}.`,
                        icon: "success",
                        confirmButtonText: '닫기'
                    });
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: '에러',
                        text: '삭제중 오류가 발생했습니다.',
                        confirmButtonText: '닫기'
                    });
                }
            }
        });
    });

    function printHTML(){
        $.ajax({
            url: "/collectpop/admin/printhtml",
            type: "get",
            success: function (result){
                console.log("통신성공!");
                let str = "";
                for(let i = 0; i < result.length; i++){
                    let dleid = result[i].dleid;
                    let dleContent = result[i].dleContent;
                    str += "<tr class='align-center'>"
                    str += "<td>"+dleid+"</td>"
                    str += "<td>"+dleContent+"</td>"
                    str += "<td><button class='btn-danger deleteBtn' data-dleid = "+dleid+">삭제</button></td>"
                    str += "</tr>"
                }
                printme.html(str);
            },
            error: function (e){
                console.log(e);
                console.log("통신실패!");
            }
        })
    }


})