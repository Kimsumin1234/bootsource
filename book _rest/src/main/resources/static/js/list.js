// 제목을 클릭시 a 태그 기능 중지
// data-id 에 있는 값 가져오기
// 부모에 이벤트를 건다
document.querySelector("tbody").addEventListener("click", (e) => {
  e.preventDefault();
  const target = e.target;

  console.log(target.dataset.id);

  // REST 방식중에서 fetch 를 쓰는방법 (화면이 깜빡이지 않고 데이터를 바로 가져올수있다)
  // 변수 대입은 빽팁 활용
  fetch(`http://localhost:8080/read/${target.dataset.id}`)
    .then((response) => response.json())
    .then((data) => {
      console.log(data);

      // 디자인 영역 가져오기
      document.querySelector("#category").value = data.categoryName;
      document.querySelector("#title").value = data.title;
      document.querySelector("#publisher").value = data.publisherName;
      document.querySelector("#writer").value = data.writer;
      document.querySelector("#price").value = data.price;
      document.querySelector("#salePrice").value = data.salePrice;
      document.querySelector("#book_id").value = data.id;
    });
});
