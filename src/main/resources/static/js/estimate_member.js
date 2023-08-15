let imgLink;
let postForm = document.getElementById("postForm");
fetch("/planner/plannerInfo")
    .then((resp) => resp.text())
    .then((body) => {

        let container = document.getElementById("plannerInfo");

        let name = document.getElementById("name").value;
        let dealCnt = document.getElementById("dealCnt").value;
        let rating =document.getElementById("rating").value;
        let plannerId = document.getElementById("planner").value;
        container.insertAdjacentHTML("afterbegin", body);

        imgLink = document.getElementById("inputimg");
        imgLink.addEventListener("click", function (e) {
            e.preventDefault();
            postForm.action = "./plannerInfo";
            postForm.method = "post";
            postForm.submit();
        });
        let inputimg = document.getElementById("inputimg");
        let BsName = document.getElementById("plannerBsName");
        let ratingAndDealCnt = document.getElementById("ratingAndDealCnt");


        BsName.innerText= name;
        let text = "평점"+rating+"/"+"최근 거래"+dealCnt+"건";
        ratingAndDealCnt.innerText = text;
        fetch('/encodeImage',{
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ id: plannerId })  // 입력된 값을 JSON 형태로 전송합니다.
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error("Network response was not ok");
                }
                return response.json();
            })
            .then(data => {
                console.log(data);
                inputimg.src = "data:image/jpeg;base64," + data.image;
            })
            .catch(error => {
                console.error('Error:', error);
                if(error.message === "Network response was not ok"){
                    alert("입력하신 정보가 올바르지 않습니다.");
                }
            });

    });

let showAButton = document.getElementById("showA");
let aaDiv = document.getElementById("A");
showAButton.addEventListener("click", function () {
    aaDiv.classList.toggle("visible");
});

let showBButton = document.getElementById("showB");
let bbDiv = document.getElementById("B");
showBButton.addEventListener("click", function () {
    bbDiv.classList.toggle("visible");
});

let showCButton = document.getElementById("showC");
let ccDiv = document.getElementById("C");
showCButton.addEventListener("click", function () {
    ccDiv.classList.toggle("visible");
});

let showDButton = document.getElementById("showD");
let ddDiv = document.getElementById("D");
showDButton.addEventListener("click", function () {
    ddDiv.classList.toggle("visible");
});

let showEButton = document.getElementById("showE");
let eeDiv = document.getElementById("E");
showEButton.addEventListener("click", function () {
    eeDiv.classList.toggle("visible");
});

let showFButton = document.getElementById("showF");
let ffDiv = document.getElementById("F");
showFButton.addEventListener("click", function () {
    ffDiv.classList.toggle("visible");
});
let chatBtn = document.getElementById("hrefChat");

chatBtn.addEventListener("click",link);

function link(){
    window.location.href="/memberChat";
}