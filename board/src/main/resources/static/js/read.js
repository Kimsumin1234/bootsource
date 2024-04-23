// 페이지 로드시 무조건 실행
// 1. 즉시실행함수
// 2. fetch() - 함수 작성후 호출

// 함수 작성
// 1. function 함수명(){}
// 2. const(or let) 이름 = () => {}
// function method1(){} ,  const method1 = () => {}   호출방법 : method1();

// 호이스팅(선안을 안해도 먼저 호출후 선언, 선호출후선언 )
// 1. function 함수명(){} : 호이스팅 가능
// 2. const(or let) 이름 = () => {} : 호이스팅 불가능
// 3. var 로 선언한 변수 : 호이스팅 가능
// 4. const, let 변수 : 호이스팅 불가능

// 날짜 포맷 변경 함수
const formatDate = (data) => {
  const date = new Date(data);

  return date.getFullYear() + "/" + (date.getMonth() + 1) + "/" + date.getDate() + " " + date.getHours() + ":" + date.getMinutes();
};

// 댓글목록 보여줄 영역 가져오기
const replyList = document.querySelector(".replyList");
// 댓글 목록 가져오기
const replyLoaded = () => {
  fetch(`/replies/board/${bno}`)
    .then((response) => response.json())
    .then((data) => {
      console.log(data);

      // 댓글 총수 바로 반영하기
      const replyCount = document.querySelector(".d-inline-block");
      console.log(replyCount);
      replyCount.innerHTML = data.length;

      let result = "";
      data.forEach((reply) => {
        result += `<div class="d-flex justify-content-between my-2 border-bottom reply-row" data-rno="${reply.rno}">`;
        result += `<div class="p-3">`;
        result += `<img src="/img/default.png" alt="" class="rounded-circle mx-auto d-block" style="width: 60px; height: 60px" />`;
        result += `</div>`;
        result += `<div class="flex-grow-1 align-self-center">`;
        result += `<div>${reply.replyer}</div>`;
        result += `<div>`;
        result += `<span class="fx-5">${reply.text}</span>`;
        result += `</div>`;
        result += `<div class="text-muted">`;
        result += `<span class="small">${formatDate(reply.createdDate)}</span>`;
        result += `</div>`;
        result += `</div>`;
        result += `<div class="d-flex flex-column align-self-center">`;
        result += `<div class="mb-2">`;
        result += `<button class="btn btn-outline-danger btn-sm">삭제</button>`;
        result += `</div>`;
        result += `<div>`;
        result += `<button class="btn btn-outline-success btn-sm">수정</button>`;
        result += `</div>`;
        result += `</div>`;
        result += `</div>`;
      });
      // 영역에 result 보여주기
      replyList.innerHTML = result;
    });
};
// 페이지 로드시 무조건 실행
replyLoaded();

// 새 댓글 등록
// 새 댓글 등록 form submit 시
// submit 기능 중지, 작성자 / 댓글 가져오기 => 스크립트 객체로 변경
const replyForm = document.querySelector("#replyForm");
replyForm.addEventListener("submit", (e) => {
  e.preventDefault();

  const replyer = replyForm.querySelector("#replyer");
  const reply = replyForm.querySelector("#reply");
  // 수정인 경우에 값이 들어옴 (수정인지 등록인지 구분하기위해서 rno가 필요)
  const rno = replyForm.querySelector("#rno");

  const data = {
    bno: bno,
    replyer: replyer.value,
    text: reply.value,
    rno: rno.value, // rno 추가
  };
  console.log(data);

  if (!rno.value) {
    // 새댓글 등록 (rno 가 없으면)
    fetch(`/replies/new`, {
      method: "post",
      headers: {
        "content-type": "application/json",
      },
      body: JSON.stringify(data),
    })
      .then((response) => response.text())
      .then((data) => {
        if (data) {
          alert(data + " 번 댓글 등록");
          // replyForm 내용제거
          // 댓글 등록하면 화면에 내용 제거하기
          replyer.value = "";
          reply.value = "";
        }
        replyLoaded(); // 이거로 새로고침을 안해도 댓글등록이 화면에 바로 확인가능
      });
  } else {
    // 댓글 수정 (rno 가 있으면), 새글 쓰는거랑 개념이 비슷하다
    fetch(`/replies/${rno.value}`, {
      method: "put",
      headers: {
        "content-type": "application/json",
      },
      body: JSON.stringify(data),
    })
      .then((response) => response.text())
      .then((data) => {
        if (data) {
          alert(data + " 번 댓글 수정");
          // replyForm 내용제거
          replyer.value = "";
          reply.value = "";
          rno.value - "";
        }
        replyLoaded(); // 이거로 새로고침을 안해도 댓글등록이 화면에 바로 확인가능
      });
  }
});

// delete
// 이벤트전파 : 자식요소에 일어난 이벤트는 상위요소로 전달 됨
// 댓글 삭제/수정 버튼 버튼 클릭 시 이벤트 전파로 찾아오기
// rno 가져오기
replyList.addEventListener("click", (e) => {
  // 실제 이벤트가 일어난 대상은 누구인가? => e.target
  const btn = e.target;

  // closest("요소") : 제일 가까운 상위요소 찾기
  const rno = btn.closest(".reply-row").dataset.rno;
  console.log("rno", rno);

  // 삭제 or 수정 버튼이 눌러졌을때만 동작
  // 삭제 or 수정 버튼이 클릭이 되었는지 구분하기
  if (btn.classList.contains("btn-outline-danger")) {
    fetch(`/replies/${rno}`, {
      method: "delete",
    })
      .then((response) => response.text())
      .then((data) => {
        if (data == "success") {
          alert("댓글 삭제 성공");
          replyLoaded();
        }
      });
  } else if (btn.classList.contains("btn-outline-success")) {
    // rno 에 해당하는 댓글을 가져온후 reply form 에 가져온내용 보여주기
    fetch(`/replies/${rno}`)
      .then((response) => response.json())
      .then((data) => {
        console.log("데이터 가져오기");
        console.log(data);

        replyForm.querySelector("#rno").value = data.rno;
        document.querySelector("#replyer").value = data.replyer;
        document.querySelector("#reply").value = data.text;
      });
  }
});
