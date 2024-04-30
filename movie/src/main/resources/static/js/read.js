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
      if (data.length > 0) {
        reviewList.classList.remove("hidden");
      }

      let result = "";
      data.forEach((review) => {
        result += `<div class="d-flex justify-content-between my-2 border-bottom py-2 review-row" data-mno="${review.mno}">`;
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

// 2. 작은 포스터 클릭하면 큰 포스터 보여주기
const imgModal = document.getElementById("imgModal");

if (imgModal) {
  imgModal.addEventListener("show.bs.modal", (e) => {
    // 모달을 뜨게 만든 li 가져오기
    const posterLi = e.relatedTarget;

    // data- 속성 가져오는방법 : dataset 을 사용해도되고
    // 아니면 그냥 속성 가져오는 방법 : getAttribute() 을 사용해도 된다
    const file = posterLi.getAttribute("data-file");
    console.log("file", file); // file 2024%5C04%5C30%2Fs_b04ffe8c-c275-4831-b7d2-7361fa90c13f_inception2.jpg

    // 모달 타이틀 영역 영화명 삽입
    imgModal.querySelector(".modal-title").textContent = `${title}`;

    // 이미지 경로 변경
    const modalBody = imgModal.querySelector(".modal-body");
    modalBody.innerHTML = `<img src="/upload/display?fileName=${file}" style="width:100%">`;
  });
}
