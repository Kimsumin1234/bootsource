// 1. /reviews/{mno}/all 요청 처리
// 날짜 포맷 변경 함수
const formatDate = (data) => {
  const date = new Date(data);
  return date.getFullYear() + "/" + (date.getMonth() + 1) + "/" + date.getDate() + " " + date.getHours() + ":" + date.getMinutes();
};

// 리뷰 목록 가져오기
// const reviewLoaded : 계속 호출이 되게끔 함수처리를 해서 함수를 호출하는 형태로 만든다
const reviewsLoaded = () => {
  fetch(`/reviews/${mno}/all`)
    .then((response) => response.json())
    .then((data) => {
      console.log(data);

      // 댓글 총수 바로 반영하기
      const reviewCount = document.querySelector(".review-cnt");
      console.log("reviewCount ", reviewCount);
      reviewCount.innerHTML = data.length;

      // 리뷰 개수가 존재한다면 reviewList 클래스명 hidden 제거
      // (리뷰유무에따라 디자인영역 보여주거나 안보여주기)
      if (data.length > 0) {
        reviewList.classList.remove("hidden");
      }

      let result = "";
      data.forEach((review) => {
        result += `<div class="d-flex justify-content-between my-2 border-bottom py-2 review-row" data-rno="${review.reviewNo}">`;
        result += `<div class="flex-grow-1 align-self-center">`;
        result += `<div><span class="font-semibold">${review.text}</span></div>`;
        result += `<div class="small text-muted">`;
        result += `<span class="d-inline-block mr-3">${review.nickname}</span>`;
        result += `평점 : <span class="grade">${review.grade}</span>`;
        result += `</div>`;
        result += `<div class="text-muted"><span class="small">${formatDate(review.createdDate)}</span></div>`;
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
      // reviewList 영역은 read.html <script> 에 찾음
      reviewList.innerHTML = result;
    });
};
// 페이지 로드시 무조건 실행
reviewsLoaded();

// 2. 리뷰등록
// 리뷰 form submit 중지
// text, grade, mid, mno
const reviewForm = document.querySelector(".review-form");
if (reviewForm) {
  reviewForm.addEventListener("submit", (e) => {
    e.preventDefault();

    const reviewNo = reviewForm.querySelector("#reviewNo");
    const mid = reviewForm.querySelector("#mid");
    const text = reviewForm.querySelector("#text");
    const nickname = reviewForm.querySelector("#nickname");

    const body = {
      mno: mno,
      mid: mid.value,
      text: text.value,
      grade: grade || 0, // grade 가 있으면 grade 를 주고 없으면 0 을 주라는 의미
      reviewNo: reviewNo.value,
    };
    console.log(body);

    // 수정이라면 reviewNo 가 존재함
    if (!reviewNo.value) {
      // 없으면 리뷰 등록
      fetch(`/reviews/${mno}`, {
        headers: {
          "content-type": "application/json",
        },
        body: JSON.stringify(body),
        method: "post",
      })
        .then((response) => response.text())
        .then((data) => {
          console.log(data);

          // 리뷰등록 초기화
          text.value = "";
          if (grade > 0) {
            reviewForm.querySelector(".starrr a:nth-child(" + grade + ")").click(); // grade 초기화
          }
          nickname.value = "";

          if (data) alert(data + " 번 리뷰가 등록되었습니다");
          reviewsLoaded(); // 댓글 리스트 다시 가져오기
        });
    } else {
      // 있으면 수정
      fetch(`/reviews/${mno}/${reviewNo.value}`, {
        method: "put",
        headers: {
          "content-type": "application/json",
        },
        body: JSON.stringify(body),
      })
        .then((response) => response.text())
        .then((data) => {
          console.log(data);

          // 리뷰등록 초기화
          text.value = "";
          reviewNo.value = "";
          if (grade > 0) {
            reviewForm.querySelector(".starrr a:nth-child(" + grade + ")").click();
          }
          nickname.value = "";

          if (data) alert(data + " 번 리뷰가 수정되었습니다");
          reviewForm.querySelector("button").innerHTML = "리뷰 등록";
          reviewsLoaded(); // 댓글 리스트 다시 가져오기
        });
    }
  });
}

// 3. 삭제 클릭시 reviewNo 가져오기
// fetch() 작성
reviewList.addEventListener("click", (e) => {
  const btn = e.target;
  console.log(btn);

  // closest("요소") : 제일 가까운 상위요소 찾기
  const reviewNo = btn.closest(".review-row").dataset.rno;
  console.log("reviewNo", reviewNo);

  // 삭제 or 수정 버튼이 눌러졌을때만 동작
  // 삭제 or 수정 버튼이 클릭이 되었는지 구분하기
  if (btn.classList.contains("btn-outline-danger")) {
    if (!confirm("리뷰를 삭제하시겠습니까?")) return;
    fetch(`/reviews/${mno}/${reviewNo}`, {
      method: "delete",
    })
      .then((response) => response.text())
      .then((data) => {
        alert(data + " 번 리뷰가 삭제되었습니다");
        reviewsLoaded();
      });
  } else if (btn.classList.contains("btn-outline-success")) {
    // 수정 버튼 누르면 리뷰폼에 내용 보여주기
    fetch(`/reviews/${mno}/${reviewNo}`)
      .then((response) => response.json())
      .then((data) => {
        console.log(data);

        reviewForm.querySelector("#reviewNo").value = data.reviewNo;
        reviewForm.querySelector("#mid").value = data.mid;
        reviewForm.querySelector("#nickname").value = data.nickname;
        reviewForm.querySelector("#text").value = data.text;

        // 이벤트 click 을 직접 호출 (별표시는 클릭해야지 나타나기 때문에)
        if (data.grade > 0) {
          reviewForm.querySelector(".starrr a:nth-child(" + data.grade + ")").click();
        }

        reviewForm.querySelector("button").innerHTML = "리뷰 수정";
      });
  }
});
