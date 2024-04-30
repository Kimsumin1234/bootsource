// 1. 수정 버튼 클릭시 (기능 중지)
// li 정보 수집해서 hidden 태그 작성 후 form 에 삽입
// upload.js 로 코드 옮김

// 2. 이미지 에 있는 x 버튼 클릭 시
// 이미지 li 만 제거
document.querySelector(".uploadResult").addEventListener("click", (e) => {
  e.preventDefault();

  console.log("x 클릭", e.target);

  // li 태그 가져오기
  const closestLi = e.target.closest("li");
  console.log("closest ", closestLi);

  // confirm 창 에서 예 버튼을 누르면 true 가 넘어감
  if (confirm("정말로 삭제 하시겠습니까?")) {
    closestLi.remove();
  }
});

// 3. 삭제버튼 클릭시 이벤트 걸기
const delBtn = document.querySelector(".btn-danger");
const actionForm = document.querySelector("#actionForm");
delBtn.addEventListener("click", () => {
  if (!confirm("정말로 삭제하시겠습니까?")) {
    return;
  }
  actionForm.submit();
});
