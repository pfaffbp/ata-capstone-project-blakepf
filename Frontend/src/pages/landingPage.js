import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import HomePageClient from "../api/homePageClient";
const map = new Map;

class HomePage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['renderHomePage'], this);
        this.dataStore = new DataStore();
    }

    async mount() {
        this.dataStore.addChangeListener(this.renderHomePage)
        this.client = new HomePageClient();
        this.renderHomePage();
    }

    async renderHomePage() {
        let workArea = document.getElementById('popular-anime-container');
        let seasonAnime = document.getElementById("seasonal-anime-container");
        let highlyRatedAnime = document.getElementById("highly-rated-container");

        let response = await this.client.getFrontPage(this.errorHandler);

        let items = "";
        let seasonalItems = "";
        let highRatedItems = "";
        console.log(response)


        for(let i =0 ; i < 10; i++){
            console.log("Inside here")
            items += `
                <div class="anime-cover-title">
                    <a href="animepage.html"><img class = "poster-click" alt=${response[i].animeId} src=${response[i].image}></a>
                        
                    <div class = "title">
                        <h5>${response[i].title}</h5>
                    </div>
                </div>
            `;
        }

        workArea.innerHTML = items;

        for(let i = 10 ; i < 20; i++){
            console.log("Inside here")
            seasonalItems += `
                <div class="anime-cover-title">
                    <a href="animepage.html"><img class = "poster-click" alt=${response[i].animeId} src=${response[i].image}></a>
                
                    <div class = "title">
                        <h5>${response[i].title}</h5>
                    </div>
                </div>
            `;
        }

        seasonAnime.innerHTML = seasonalItems;

        for(let i = 20 ; i < 30; i++){
            console.log("Inside here")
            highRatedItems += `
                <div class="anime-cover-title">
                    <a href="animepage.html"><img class = "poster-click" alt=${response[i].animeId} src=${response[i].image}></a>
                
                    <div class = "title">
                        <h5>${response[i].title}</h5>
                    </div>
                </div>
            `;
        }

        highlyRatedAnime.innerHTML = highRatedItems;

        let docs = document.querySelectorAll('.poster-click');

        docs.forEach(href => {
            href.addEventListener('click', async function (){
                // datasset("animeCode", href.value);
                sessionStorage.setItem("animeCode", href.alt);
            });
        });
        await console.log(sessionStorage.getItem("animeCode")); // stores the animeId to get data;
        console.log(docs);
    }

}

const myslide = document.querySelectorAll('.myslide'),
	  dot = document.querySelectorAll('.dot');
let counter = 1;
slidefun(counter);

let timer = setInterval(autoSlide, 8000);
function autoSlide() {
	counter += 1;
	slidefun(counter);
}
function plusSlides(n) {
	counter += n;
	slidefun(counter);
	resetTimer();
}
function currentSlide(n) {
	counter = n;
	slidefun(counter);
	resetTimer();
}
function resetTimer() {
	clearInterval(timer);
	timer = setInterval(autoSlide, 8000);
}

function slidefun(n) {
	
	let i;
	for(i = 0;i<myslide.length;i++){
		myslide[i].style.display = "none";
	}
	for(i = 0;i<dot.length;i++) {
		dot[i].className = dot[i].className.replace(' active', '');
	}
	if(n > myslide.length){
	   counter = 1;
	   }
	if(n < 1){
	   counter = myslide.length;
	   }
	myslide[counter - 1].style.display = "block";
	dot[counter - 1].className += " active";
}

window.addEventListener('DOMContentLoaded', main);








