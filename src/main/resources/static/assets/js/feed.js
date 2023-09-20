// infinite scroll
$(document).ready(function (){
    //add feed script
    const btn = document.getElementById('picon');
    const modal = document.getElementById('addModalWrap');
    const closeBtn = document.getElementById('closeBtn');
    printHTML();
    btn.addEventListener('click',function (e){
        console.log("btn 클릭이벤트");
        modal.classList.toggle('opaque');
        modal.classList.toggle('unstaged');
    });
    closeBtn.addEventListener('click',function (e){
        modal.classList.toggle('opaque');

        modal.addEventListener('transitionend', function fact(e){
            this.classList.toggle('unstaged');
            this.removeEventListener('transitionend', fact);
        });
    })
    // End of add feed script
    // view feed script
    const viewImg = document.getElementById("viewImg");
    const viewImg2 = document.getElementById("viewImg2");
    const viewImgBox1 = document.getElementById("viewImgBox");
    const viewImgBox2 = document.getElementById("viewImgBox2");
    const nextBtn = viewImg.getElementsByClassName("nextBtn")[0];
    const prevBtn = viewImg.getElementsByClassName("prevBtn")[0];
    const nextBtn2 = viewImg2.getElementsByClassName("nextBtn")[0];
    const prevBtn2 = viewImg2.getElementsByClassName("prevBtn")[0];
    const likesBtn = document.querySelector(".likes");
    const viewCloseBtn = document.getElementById('viewCloseBtn');
    const viewImgBox = document.getElementById("viewImgBox");
    const mediaQuery = window.matchMedia('(max-width: 610px)');
    const likesCount = document.getElementById("likes");
    const replyBtn = document.getElementById("replyBtn");
    const replyContent = document.getElementById("replyContent");
    const replyCountDiv = document.getElementById("replies");
    const deletebtn = document.getElementById("deleteBtn");
    const viewprofileimg = document.querySelectorAll(".viewprofileImg");
    console.log("viewprofileimg" + viewprofileimg);
    const decDetailBtn = document.getElementsByClassName("decDetail");
    let num = 0;
    let count = 0;
    let feedLikesCount = 0;
    let viewBtnDataVal = 0;
    let userId = 0
    function viewModalEvent(){
        const viewBtn = document.getElementsByClassName('feedimgBox');
        const viewModal = document.getElementById('viewModalWrap');
        console.log(viewBtn);
        console.log(viewBtn.length);
        console.log("feedLikesCount = " + feedLikesCount);
        for (let i = 0; i < viewBtn.length; i++) {
            viewBtn[i].addEventListener('click', function (e) {
                viewBtnDataVal = viewBtn[i].dataset.fid;
                deletebtn.dataset.fid = viewBtnDataVal;
                viewReplies(e);
                console.log("feedimgBox 클릭!!");
                console.log("viewBtnDataVal = " + viewBtnDataVal);
                likesBtn.removeEventListener("click", likesHandler);
                likesBtn.addEventListener("click", likesHandler);
                likesBtn.dataset.fid = viewBtnDataVal;
                viewModal.classList.toggle('opaque');
                viewModal.classList.toggle('unstaged')

                let data = {
                    fid : viewBtnDataVal
                }
                $.ajax({
                    url: "/collectpop/getFeedImg",
                    type: "post",
                    data: data,
                    success: function (result) {
                        console.log("success!!");
                        console.log("result" + result);
                        const images = result.images;
                        let hashTags = [];
                        hashTags = result.hashTag;
                        const regDate  = result.regDate;
                        userId = result.feedUserId;
                        const userprofile = result.userFileName;
                        console.log("userprofile = " + userprofile);
                        console.log("userId = " + userId);
                        console.log("해쉬테그 = " + hashTags);
                        const hashLength = hashTags.length;
                        console.log("해쉬테그길이 = " + hashLength);
                        console.log(result.images);
                        const resultLength = images.length;
                        feedLikesCount = result.feedLikes;
                        const replyCount = result.replyCount;
                        console.log("replyCount = " + replyCount);
                        console.log("feedLikesCount = " + feedLikesCount);
                        viewImg.dataset.resultLength = resultLength;
                        console.log("resultLength = " + resultLength);
                        viewImgBox1.innerHTML = '';
                        viewImgBox2.innerHTML = '';
                        replyContent.value = "";
                        for(let i = 0; i < resultLength; i++){
                            let imgsrc = images[i].fileName;
                            let img1 = document.createElement("img");
                            img1.src = "/collectpop/images/" + imgsrc;
                            img1.classList.add("viewFeedImg");
                            img1.alt = "피드이미지";
                            viewImgBox1.appendChild(img1);

                            let img2 = document.createElement("img");
                            img2.src = "/collectpop/images/" + imgsrc;
                            img2.classList.add("viewFeedImg2");
                            img2.alt = "피드이미지";
                            viewImgBox2.appendChild(img2);
                        }
                        if(resultLength > 1){
                            nextBtn.classList.remove("hidden");
                            nextBtn2.classList.remove("hidden");
                            imgslide()
                        }
                        const writerName1 = document.getElementsByClassName("writerNickName")[0];
                        const writerName2 = document.getElementById("writerNickName");
                        writerName1.innerText = result.userNickName;
                        writerName2.innerHTML = ""+result.userNickName+" &nbsp; <span id=\"writerContent\" style=\"font: 300 13px '';\">"+result.feedContent+"</span><br> - 2주"
                        console.log("feedcontent = " + result.feedContent);
                        likesCount.innerHTML = "좋아요 : <span>"+feedLikesCount+"</span>";
                        replyCountDiv.innerHTML = "댓글 : <span>"+replyCount+"</span>"
                        let hashTagContent = "";
                        if(hashTags.length === 0){
                            hashTagContent = "";
                        }else{
                            for (let i = 0; i < hashLength; i++) {
                                if(hashTags[i] && hashTags[i].tagName){
                                    let hashTagName = hashTags[i].tagName;
                                    console.log("해쉬 = #" + hashTagName);
                                    hashTagContent += "#" + hashTagName + "&nbsp;";
                                }
                            }
                        }
                        writerName2.innerHTML = ""+result.userNickName+" &nbsp; <span id=\"writerContent\" style=\"font: 300 13px '';\">"+result.feedContent + "&nbsp;" + hashTagContent + "</span><br> - "+regDate+"";
                        for (let j = 0; j < viewprofileimg.length; j++) {
                            viewprofileimg[j].src = "/collectpop/userimages/" + userprofile;
                        }
                        if(result.userCheck){
                            document.getElementById("userCheck").style.display = "block";
                        }
                        },
                    error: function (e){
                        console.log("failed..");
                        console.log(e);
                    },
                    complete: function (){

                    }
                })

            });

        }
    }



    function likesHandler(e){
        console.log(likesBtn);
        const clickedBtn = e.currentTarget;
        let userIdString = document.getElementById("userNickName").dataset.userid;
        console.log("userIdString = " + userIdString);
        let userId = parseInt(userIdString);
        console.log("userId = " + userId);
        let data = {
            fid: likesBtn.dataset.fid,
            userId: userId
        }
        $.ajax({
            url: "/collectpop/likes",
            type: "post",
            data: data,
            success: function (result) {
                console.log("likes success!!")
                console.log(result);
                let isLiked = result === 0;
                if(isLiked){
                    feedLikesCount++;
                }else{
                    feedLikesCount--;
                }
                likesCount.innerHTML = "좋아요 : <span>" + feedLikesCount + "</span>";

            },
            error: function (e) {
                console.log("통신 실패!");
                console.log(e);
            }
        })
    }

    const repliesBox = document.getElementById("replyBox");

    replyBtn.addEventListener("click",function (e){
        const replyContent = document.getElementById("replyContent");
        let replyContentVal = replyContent.value;
        let userIdString = document.getElementById("userNickName").dataset.userid;
        console.log("userIdString = " + userIdString);
        let userId = parseInt(userIdString);
        console.log("userId = " + userId);
        if(!userId){
            Swal.fire({
                text: '로그인후 이용 가능합니다.',
                icon: 'error',
                confirmButtonText: '확인'
            })
            return;
        }
        if(replyContentVal === ""){
            Swal.fire({
                text: '댓글을 작성해 주세요.',
                icon: 'error',
                confirmButtonText: '확인'
            })
            return;
        }

        let data = {
            fid : viewBtnDataVal,
            userId : userId,
            content: replyContentVal
        }
        console.log(data);
        $.ajax({
            url: "/collectpop/replies",
            type: "post",
            data: data,
            success: function (result) {
                console.log("통신성공 !!" + result);
                replyContent.value = "";
            },
            error: function (e) {
                console.log(e);
                console.log("통신 실패");
            },
            complete: function () {
                viewReplies(e);
            }
        })
    })

    function viewReplies(e){
        let data = {
            fid : viewBtnDataVal
        }
        console.log(data);
        $.ajax({
            url: "/collectpop/getReplies",
            type: "get",
            data: data,
            success: function (result){
                console.log("통신성공!!");
                console.log(result);
                let str = "";
                for (let i = 0; i < result.length; i++) {
                    let replyDate = result[i].regDate;
                    let userNickName = result[i].userNickName;
                    let userProfile = result[i].userProfile;
                    const calculDate = getTimeAgo(replyDate);
                    str += '<div class="replies">'
                    str += '<img class="viewprofileImg" src="/collectpop/userimages/'+userProfile+'" alt="프로필 이미지">'
                    str += '<h6 id="writerNickName">'+userNickName+' &nbsp;&nbsp; <span style="font: 300 13px \'\';">'+result[i].content+'</span><br> - '+calculDate+'</h6>'
                    str += '</div>'
                }
                repliesBox.innerHTML = str;
            },
            error: function (e){
                console.log(e)
                console.log("통신실패!")
            }
        })
    }

    function getTimeAgo(replyDate) {
        const start = new Date(replyDate);
        const end = new Date();
        const timeDiffInSeconds = Math.floor((end - start) / 1000);

        const years = Math.floor(timeDiffInSeconds / (60 * 60 * 24 * 365));
        const months = Math.floor(timeDiffInSeconds / (60 * 60 * 24 * 30));
        const weeks = Math.floor(timeDiffInSeconds / (60 * 60 * 24 * 7));
        const days = Math.floor(timeDiffInSeconds / (60 * 60 * 24));
        const hours = Math.floor(timeDiffInSeconds / (60 * 60));
        const minutes = Math.floor(timeDiffInSeconds / 60);

        if (years > 0) {
            return years + "년 전";
        } else if (months > 0) {
            return months + "달 전";
        } else if (weeks > 0) {
            return weeks + "주 전";
        } else if (days > 0) {
            return days + "일 전";
        } else if (hours > 0) {
            return hours + "시간 전";
        } else if (minutes > 0) {
            return minutes + "분 전";
        } else if (timeDiffInSeconds > 30) {
            return timeDiffInSeconds + "초 전";
        } else {
            return "방금 전";
        }
    }

    let isImgSlideRegistered = false;
    function imgslide(){
        count = viewImg.dataset.resultLength - 1;
        if(!isImgSlideRegistered){
            console.log(viewImgBox);
            nextBtn.addEventListener("click", function (e){
                num++;
                viewImgBox.style.left = -1 * (100 * 6) * num + "px";
                if(mediaQuery.matches){
                    viewImgBox2.style = -1 * (10 * 37) * num + "px";
                }else {
                    viewImgBox2.style.left = -1 * (10 * 54) * num + "px";
                }
                console.log(num);
                console.log(viewImgBox.style.left);
                console.log(count);
                if(num === count){
                    nextBtn.classList.add("hidden");
                    nextBtn2.classList.add("hidden");
                }
                if(num > 0){
                    prevBtn.classList.remove("hidden");
                    prevBtn2.classList.remove("hidden");
                }
            });
            nextBtn2.addEventListener("click", function (e){
                num++;
                viewImgBox.style.left = -1 * (100 * 6) * num + "px";
                if(mediaQuery.matches){
                    viewImgBox2.style = -1 * (10 * 37) * num + "px";
                }else {
                    viewImgBox2.style.left = -1 * (10 * 54) * num + "px";
                }
                console.log(num);
                console.log(viewImgBox.style.left);
                console.log(count);
                if(num === count){
                    nextBtn.classList.add("hidden");
                    nextBtn2.classList.add("hidden");
                }
                if(num > 0){
                    prevBtn.classList.remove("hidden");
                    prevBtn2.classList.remove("hidden");
                }
            });
            prevBtn.addEventListener("click",function (e){
                num--;
                viewImgBox.style.left = -1 * (100 * 6) * num + "px";
                if(mediaQuery.matches){
                    viewImgBox2.style = -1 * (10 * 37) * num + "px";
                }else {
                    viewImgBox2.style.left = -1 * (10 * 54) * num + "px";
                }
                console.log(num);
                console.log(viewImgBox.style.left);
                console.log(count);

                if(num < count){
                    nextBtn.classList.remove("hidden");
                    nextBtn2.classList.remove("hidden");
                }
                if(num === 0){
                    prevBtn.classList.add("hidden");
                    prevBtn2.classList.add("hidden");
                }
            });
            prevBtn2.addEventListener("click",function (e){
                num--;
                viewImgBox.style.left = -1 * (100 * 6) * num + "px";
                if(mediaQuery.matches){
                    viewImgBox2.style = -1 * (10 * 37) * num + "px";
                }else {
                    viewImgBox2.style.left = -1 * (10 * 54) * num + "px";
                }
                console.log(num);
                console.log(viewImgBox.style.left);
                console.log(count);

                if(num < count){
                    nextBtn.classList.remove("hidden");
                    nextBtn2.classList.remove("hidden");
                }
                if(num === 0){
                    prevBtn.classList.add("hidden");
                    prevBtn2.classList.add("hidden");
                }
            });
            isImgSlideRegistered = true;
        }

    }

    let isViewCloseBtnRegistered = false;
    function registerViewCloseBtnEventHandler() {
        const viewModal = document.getElementById('viewModalWrap');
        if (!isViewCloseBtnRegistered) {
            viewCloseBtn.addEventListener('click', function (e) {
                console.log("viewCloseBtn 클릭!")
                viewImgBox.style.left = 0;
                viewImgBox2.style.left = 0;
                num = 0;
                count = 0;
                nextBtn.classList.add("hidden");
                nextBtn2.classList.add("hidden");
                prevBtn.classList.add("hidden");
                prevBtn2.classList.add("hidden");
                viewModal.classList.toggle('opaque');
                viewModal.removeEventListener("click", viewModalEvent);
                viewModal.addEventListener('transitionend', function viewFact(e) {
                    this.classList.toggle('unstaged');
                    this.removeEventListener('transitionend', viewFact);
                    console.log("viewModal에 이벤트 제거")
                });
            });


            isViewCloseBtnRegistered = true;
        }
    }
    // End of view feed script
    // declaration modal script
    let isFeedManuModalRegistered = false;
    function registerFeedManuModalEventHandler(){
        const feedmanu = document.getElementById('feedmanu');
        const feedManuModalWrap = document.getElementById('feedManuModalWrap');
        const feedManuCloseBtn = document.getElementById('feedManuCloseBtn')
        if(!isFeedManuModalRegistered) {
            feedmanu.addEventListener('click', function (e) {
                console.log("메뉴 버튼 클릭!!")

                feedManuModalWrap.classList.toggle('opaque');
                feedManuModalWrap.classList.toggle('unstaged');
            })
            feedManuCloseBtn.addEventListener('click', function (e) {
                feedManuModalWrap.classList.toggle('opaque');

                feedManuModalWrap.addEventListener('transitionend', function viewFact(e) {
                    this.classList.toggle('unstaged');
                    this.removeEventListener('transitionend', viewFact);
                });

            });
            isFeedManuModalRegistered = true;
        }
    }
    // End of declaration modal script
    // declaration modal script
    let isdecModalRegistered = false;
    function registerDecModalEventHandler(){
        const decBtn = document.getElementById('decBtn');
        const decModal = document.getElementById('decModalWrap');
        const decModalCloseBtn = document.getElementById('decModalCloseBtn')
        if(!isdecModalRegistered) {
            decBtn.addEventListener('click', function (e) {
                console.log("신고 버튼 클릭!!")

                decModal.classList.toggle('opaque');
                decModal.classList.toggle('unstaged');
            })
            decModalCloseBtn.addEventListener('click', function (e) {
                decModal.classList.toggle('opaque');

                decModal.addEventListener('transitionend', function viewFact(e) {
                    this.classList.toggle('unstaged');
                    this.removeEventListener('transitionend', viewFact);
                });

            });
            isdecModalRegistered = true;
        }
    }
    // End of declaration modal script
    // infinite scroll start
    let feedcontenttag = $("#feedcontent");
    let page = 1;
    let lastScroll = 0;
    let isLoading = false;
    let hasMoreData = true;
    let searchVal = "";
    const searchBox = document.getElementById("sinput");
    searchBox.addEventListener("keydown",function (e){
        if(e.key === "Enter"){
            console.log("엔터키 누름!!")
            searchVal = searchBox.value;
            console.log("searchVal = " + searchVal);
            resetDataAndLoad();
        }
    })
    function resetDataAndLoad() {
        feedcontenttag.html(''); // 기존 데이터 초기화
        page = 1; // 페이지 초기화
        hasMoreData = true; // 데이터 추가 여부 초기화
        loadMoreData();
    }
    function loadMoreData(){
        if (isLoading || !hasMoreData) {
            return;
        }
        isLoading = true;
        $.ajax({
            url: '/collectpop/items',
            type: 'get',
            data: {
                page : page,
                keyword : searchVal
            },
            success: function (result){
                if(result.length === 0){
                    hasMoreData = false;
                    console.log("데이터 다 불러옴!!");
                    isLoading = false;
                    $(document).off('scroll');
                    return;
                }
                console.log("통신성공");
                console.log(result);
                let str = feedcontenttag.html();
                for(let i = 0; i < result.length; i++){
                    let imgsrc = result[i].fileName;
                    let imgFid = result[i].fid;
                    console.log(imgsrc);
                    console.log(imgFid);
                    str += "<div class='feedimgBox' data-fid='" +imgFid+ "'>"
                    str += "<img class='feedimg' src='/collectpop/images/"+ imgsrc +"' alt='피드 이미지'>"
                    str += "</div>"
                }
                    feedcontenttag.html(str);

                setTimeout(function (){
                    $('.feedimgBox').addClass("with-transition");
                },300)
                viewModalEvent() // 데이터가 있는경우에만 viewModalEvent를 호출
            },
            error : function (e){
                console.log('통신실패');
                console.log(e);
            },
            complete: function (){
                page ++;
                isLoading = false;
                registerViewCloseBtnEventHandler()
                registerFeedManuModalEventHandler()
                deleteHandler()
                registerDecModalEventHandler()
            }
        })
    }

    resetDataAndLoad();
    $(document).scroll(function(e){
        //현재 높이 저장
        const currentScroll = $(this).scrollTop();
        //전체 문서의 높이
        const documentHeight = $(document).height();

        //(현재 화면상단 + 현재 화면 높이)
        const nowHeight = $(this).scrollTop() + $(window).height();


        //스크롤이 아래로 내려갔을때만 해당 이벤트 진행.
        if(currentScroll > lastScroll){

            //nowHeight을 통해 현재 화면의 끝이 어디까지 내려왔는지 파악가능
            //즉 전체 문서의 높이에 일정량 근접했을때 글 더 불러오기)
            if(documentHeight < (nowHeight + (documentHeight*0.1))){
                loadMoreData();
            }
        }

        //현재위치 최신화
        lastScroll = currentScroll;

    });
    //End of infinite scroll


    // drop file script
    // DropFile 함수 정의
    // 파일이 드롭되었을 때 또는 input으로 추가 되었을 때 처리
    let dropFiles = [];

    function DropFile(dropAreaId, fileListId) {
        // HTML에서 드롭 영역과 파일 목록을 가져오기 위해 요소들을 식별합니다.
        let dropArea = document.getElementById(dropAreaId);
        let fileList = document.getElementById(fileListId);
        // 이벤트 기본 동작을 막는 함수
        function preventDefaults(e) {
            e.preventDefault();
            e.stopPropagation();
        }

        // 드래그 영역 하이라이트 표시
        function highlight(e) {
            preventDefaults(e);
            dropArea.classList.add("highlight");
        }

        // 드래그 영역 하이라이트 제거
        function unhighlight(e) {
            preventDefaults(e);
            dropArea.classList.remove("highlight");
        }


        function handleDrop(e) {
            unhighlight(e);
            let dt = e.dataTransfer;
            let files = dt.files;

            dropFiles = dropFiles.concat(Array.from(files));

            // 선택한 파일들 처리
            handleFiles(files);

            // 파일 목록을 스크롤하여 가장 최근 파일을 볼 수 있도록 이동
            const fileList = document.getElementById(fileListId);
            if (fileList) {
                fileList.scrollTo({ top: fileList.scrollHeight });
            }
        }
        function chooseInput(files){
            const chooseFiles = files;
            for(let i = 0; i < chooseFiles.length; i++){
                const file = chooseFiles[i];
                console.log("input으로 추가한 file = " + file);
                dropFiles.push(file);
                console.log(dropFiles.length);
            }
            handleFiles(files);
        }
        // 선택한 파일들 처리
        function handleFiles(files) {
            files = [...files];
            files.forEach(previewFile); // 각 파일에 대해 미리보기 생성
        }

        // 파일 미리보기 생성
        function previewFile(file) {
            console.log(file);
            renderFile(file);
        }

        // 파일 렌더링 (미리보기 생성)
        function renderFile(file) {
            const imgList = document.getElementById('imgList');
            let reader = new FileReader();
            reader.readAsDataURL(file);
            reader.onloadend = function () {
                let img = dropArea.getElementsByClassName("preview")[0];
                img.src = reader.result;
                img.style.display = "block";

                addimgList(imgList, reader, file, img);
                RegisterfileListBtnClickEventHandler(imgList);

                // 파일 레이블 표시 제어
                let labels1 = dropArea.getElementsByClassName("file-label1");
                if (labels1.length > 0) {
                    labels1[0].style.display = "none";
                }

            };
        }
        let fileListBtnClickRegistered = false;
        function RegisterfileListBtnClickEventHandler(imgList){
            if(!fileListBtnClickRegistered){
                const fileListBtn = document.getElementById("fileList");
                if(fileListBtn){
                    fileListBtn.style.display = "block";
                }

                if(dropFiles.length > 0){
                    imgList.style.display = "flex";
                    imgList.classList.add("open");
                }else if(dropFiles.length === 0){
                    if(imgList.classList.contains("open")){
                        imgList.style.display = "none";
                        imgList.classList.remove("open");
                    }
                }
                fileListBtn.addEventListener('click',function (e){
                    console.log("fileListBtn 클릭!")
                    if(imgList.classList.contains('open')){
                        imgList.style.display = "none";
                        imgList.classList.remove("open");
                    }else {
                        imgList.style.display = "flex";
                        imgList.classList.add("open");

                    }
                });
            }
            fileListBtnClickRegistered = true;
        }

        let imgIndex = 0;
        function addimgList(imgList, reader, file, img){
            //이미지 리스트에 추가
            // div 형태 그대로 만들어 imgList안에 추가
            let newDiv = document.createElement("div");
            newDiv.classList.add("imgListBox");
            newDiv.dataset.fileName = file.name;
            let newImg = document.createElement("img");
            newImg.classList.add("addimg");
            newDiv.dataset.index = imgIndex;
            imgIndex++;
            newImg.src = reader.result;
            newDiv.appendChild(newImg);
            let deleteDiv = document.createElement("div");
            deleteDiv.classList.add("deleteBtn");
            newDiv.appendChild(deleteDiv);
            imgList.appendChild(newDiv);

            newDiv.addEventListener("mouseover",function (e){
                deleteDiv.style.display = "block";
            });
            newDiv.addEventListener("mouseleave",function (e){
                deleteDiv.style.display = "none";
            });
            deleteDiv.addEventListener("mouseover",function (e){
                deleteDiv.style.display = "block";

            });
            deleteDiv.addEventListener("mouseleave",function (e){
                deleteDiv.style.display = "none";
            });
            newImg.addEventListener("click", function (e){
                const allimgs = imgList.querySelectorAll(".addimg");
                allimgs.forEach(image => image.style.filter = "brightness(0.5)");
                console.log(allimgs);
                e.target.style.filter = "brightness(1)";
                console.log(e.target);
                img.src = e.target.src;
            })
            deleteImg(deleteDiv, reader, imgList, img)
        }

        // 누적 이미지 삭제
        function deleteImg(deleteDiv, img){
            deleteDiv.addEventListener("click",function (e){
                const parentDiv = this.parentElement;
                const div = deleteDiv.parentNode;
                const imgIndex = parseInt(div.dataset.index);
                console.log(parentDiv);
                if(parentDiv){
                    const fileName = parentDiv.dataset.fileName;
                    console.log("parentDiv의 fileName = " + fileName);

                    const index = dropFiles.findIndex(file => file.name === fileName);
                    if(index !== -1){
                        dropFiles.splice(index, 1);
                    }

                    parentDiv.remove();
                    const nextImg = document.querySelector(`[data-index="${imgIndex + 1}"] .addimg`);
                    if(nextImg){
                        img.src = nextImg.src;
                    }
                    console.log(dropFiles.length);
                    console.log(dropFiles);
                }

            });
        }

        // 드래그 앤 드롭 이벤트 리스너 추가
        dropArea.addEventListener("dragenter", highlight, false);
        dropArea.addEventListener("dragover", highlight, false);
        dropArea.addEventListener("dragleave", unhighlight, false);
        dropArea.addEventListener("drop", handleDrop, false);

        // DropFile 객체 반환
        return {
            chooseInput
        };
    }
    // DropFile 객체 생성 및 초기화
    window.dropFile = new DropFile("drop-file", "files");

    // End of drop file script

    const feeduploadBtn = document.getElementById("uploadSubmitBtn");
    const input = document.querySelector('#feedHashTag');
    console.log(input);
    const tagify = new Tagify(input,{
        originalInputValueFormat: valuesArr => valuesArr.map(item => item.value).join(",")
    });
    // 태그가 추가되면 이벤트 발생
    tagify.on('add', function() {
        console.log(tagify.value); // 입력된 태그 정보 객체
    })
    feeduploadBtn.addEventListener("click",function (e){
        if(dropFiles.length === 0){
            Swal.fire({
                text: "이미지를 등록해주세요.",
                icon: 'error',
                confirmButtonText: '확인'
            })
            return;
        }
        uploadFeed();
    })

    function uploadFeed(){
        let userIdString = document.getElementById("userNickName").dataset.userid;
        const feedTextVal = document.getElementById("feedText").value;
        if(feedTextVal === ""){
            Swal.fire({
                text: "내용을 작성해주세요.",
                icon: 'error',
                confirmButtonText: '확인'
            })
            return;
        }
        console.log("userIdString = " + userIdString);
        let userId = parseInt(userIdString);
        console.log("userId = " + userId);
        let formData = new FormData();
        formData.append("userId", userId);
        formData.append("content", feedTextVal);
        formData.append("values", input.value);
        for (let i = 0; i < dropFiles.length; i++) {
            formData.append("imageFiles", dropFiles[i]);
        }
        console.log(feedTextVal);
        console.log(dropFiles);
        console.log(input.value);

        $.ajax({
            url: "/collectpop/addfeed",
            type: "post",
            data: formData,
            contentType: false,
            processData: false,
            success: function (result){
                console.log("통신성공!");
                console.log(result);
                if(result === "success"){
                    modal.classList.toggle('opaque');

                    modal.addEventListener('transitionend', function fact(e){
                        this.classList.toggle('unstaged');
                        this.removeEventListener('transitionend', fact);
                    });

                    location.reload();

                }
            },
            error: function (e) {
                console.log("통신 실패..");
                console.log(e);
            }
        })
    }
    const deletefeedBtn = document.getElementById("deletefeed");
    const deleteCheck = document.getElementById("deleteCheck");
    const deletemodalclose = document.getElementById("deletemodalclose");
    let idDeleteFeedRegistered = false;
    function deleteHandler() {
        if(!idDeleteFeedRegistered) {
            deletefeedBtn.addEventListener('click', function (e) {
                console.log("메뉴 버튼 클릭!!")

                deleteCheck.classList.toggle('opaque');
                deleteCheck.classList.toggle('unstaged');
            })
            deletemodalclose.addEventListener('click', function (e) {
                deleteCheck.classList.toggle('opaque');

                deleteCheck.addEventListener('transitionend', function viewFact(e) {
                    this.classList.toggle('unstaged');
                    this.removeEventListener('transitionend', viewFact);
                });

            });
        }
    }
    deletebtn.addEventListener("click", function (e){
        const deletefid = deletebtn.dataset.fid;
        console.log("deleteBtn click!! fid = " + deletefid);
        $.ajax({
            url: "/collectpop/deletefeed",
            type: "post",
            data: {fid : deletefid},
            success: function (result){
                console.log("통신성공!!");
                console.log(result);
                location.reload();
            },
            error: function (e) {
                console.log(e);
                console.log("통신실패!")
            }
        })
    })
    const printme = document.getElementById("decModalContent");

    function printHTML(){
        $.ajax({
            url: "/collectpop/admin/printhtml",
            type: "get",
            success: function (result){
                console.log("통신성공!");
                let str = "";
                str += '<div class="decContent">'
                str += '<h4>신고<span id="decModalCloseBtn">x</span></h4>'
                str += '</div>'
                str += '<div class="decContent">'
                str += '<h5>이 글을 신고하는 이유?</h5>'
                str += '</div>'
                for(let i = 0; i < result.length; i++){
                    let dleid = result[i].dleid;
                    let dleContent = result[i].dleContent;
                    str += '<div class=\"decContent\">'
                    str += '<button class="decDetail" value="'+dleid+'">'+dleContent+'<span class="sendIcon"><i class="bi bi-caret-right"></i></span></button>'
                    str += '</div>'
                }
                printme.innerHTML = str;
            },
            error: function (e){
                console.log(e);
                console.log("통신실패!");
            }
        })
    }
    $(document).on("click", ".decDetail", function() {
        let dleid = $(this).val();
        console.log("fid = " + viewBtnDataVal);
        console.log("dleid = " + dleid);
        console.log("userId = " + userId);
        let declaration = {
            fid : viewBtnDataVal,
            dleid : dleid,
            userId : userId

        }
        Swal.fire({
            icon: "warning",
            title: '정말 신고 하시겠습니까?',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '신고',
            cancelButtonText: '취소',
            preConfirm: () => {
                return $.ajax({
                    url: "/collectpop/admin/adddec",
                    type: "post",
                    data: declaration, // 입력된 텍스트 데이터를 서버로 보냄
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
                        text: '신고 접수중 오류가 발생했습니다.',
                        confirmButtonText: '닫기'
                    });
                }
            }
        });
    });
})//End of dom.ready




