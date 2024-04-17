// 삭제버튼 클릭시 actionForm 전송
const button = document.querySelector(".btn-danger");
const form = document.querySelector("#actionForm");
button.addEventListener("click", () => {
  if (!confirm("정말로 삭제하시겠습니다?")) return;
  form.action = "/board/remove";
  form.submit();
});
