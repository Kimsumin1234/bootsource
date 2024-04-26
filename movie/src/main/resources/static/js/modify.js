// 삭제버튼 클릭시 이벤트 걸기
const delBtn = document.querySelector(".btn-danger");
const actionForm = document.querySelector("#actionForm");
delBtn.addEventListener("click", () => {
  if (!confirm("정말로 삭제하시겠습니까?")) {
    return;
  }
  actionForm.submit();
});
