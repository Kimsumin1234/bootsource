// 2. 작은 포스터 클릭하면 큰 포스터 보여주기
const imgModal = document.getElementById("imgModal");
console.log("imgModal", imgModal);

if (imgModal) {
  imgModal.addEventListener("show.bs.modal", (e) => {
    // 모달을 뜨게 만든 li 가져오기
    const posterLi = e.relatedTarget;
    console.log("posterLi", posterLi);

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
