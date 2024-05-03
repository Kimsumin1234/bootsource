// 1. 등록버튼 누르면 이벤트발생
// upload.js 로 코드 옮김

// 2. x 를 누르면 파일 삭제 요청
document.querySelector(".uploadResult").addEventListener("click", (e) => {
  e.preventDefault();
  console.log("x 클릭", e.target);
  console.log("x 클릭", e.currentTarget);

  const closestLi = e.target.closest("li");
  console.log("closest ", closestLi);

  // data-file 에 있는 값 가져오기
  const filePath = e.target.closest("a").dataset.file;
  console.log(filePath);

  // 폼태그 작성해서 filePath 를 담는다
  const formData = new FormData();
  formData.append("filePath", filePath);

  fetch(`/upload/remove`, {
    method: "post",
    headers: {
      "X-CSRF-TOKEN": csrfValue,
    },
    body: formData,
  })
    .then((response) => response.text())
    .then((data) => {
      console.log(data);
      if (data) {
        alert("이미지 파일 삭제 완료");
        // 해당 li 제거
        closestLi.remove();
      }
    });
});
