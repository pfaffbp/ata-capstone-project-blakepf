import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import HomePageClient from "../api/homePageClient";
import AnimeClient from "../api/animeClient";

<<<<<<< HEAD
class SearchPage extends BaseClass {
=======
class SearchPage extends BaseClass{

>>>>>>> origin/juvisbranch
    constructor() {
        super();
        this.bindClassMethods(['renderSearchPage', 'searchByGenre', 'animateKero'], this);
        this.dataStore = new DataStore();
        this.genreButtons = Array.from(document.querySelectorAll('.button-86'));
        this.keroElement = document.getElementById('kero');
        this.animateKero = this.animateKero.bind(this);
        this.animateKero();

    }

    async mount() {
        document.getElementById("search-form").addEventListener("submit", this.renderSearchPage);
<<<<<<< HEAD
        this.genreButtons.forEach(button => button.addEventListener("click", this.searchByGenre));
        this.keroElement.addEventListener("animationstart", this.animateKero);
        this.client = new AnimeClient();
    }

    async renderSearchPage(event) {
        event.preventDefault();
=======
        document.getElementById("genre-form").addEventListener("submit", this.renderSearchPage);
        this.client = new AnimeClient();
    }


    async renderSearchPage(event) {
        event.preventDefault();

>>>>>>> origin/juvisbranch
        let workArea = document.getElementById("work-area");
        let searchValue = document.getElementById("search-bar").value;
        let genreValue = document.getElementById("genre-bar").value;
        let searchValueIntoString = `${searchValue}`;
        let genreIntoString = `${genreValue}`;

        console.log(searchValue);
        console.log(genreValue);

        // const newResponse = await this.client.getAnimeBySearch(searchValueIntoString, this.errorHandler);
        const newResponse = await this.client.getAnimeByGenre(genreIntoString, this.errorHandler);

        let items = "";

        console.log(newResponse[0]);
        for (let i = 0; i < newResponse.length; i++) {
            items += `
        <div class="poster">
          <a href="animepage.html"><img class="poster-click" alt=${newResponse[i].id} src=${newResponse[i].coverImage.large}></a>  
          <p>${newResponse[i].title.userPreferred}</p>
        </div>
      `;
        }

<<<<<<< HEAD
        const style = document.createElement('style');
        style.innerHTML = ``;
=======
      
>>>>>>> origin/juvisbranch

        workArea.innerHTML = items;

        document.querySelectorAll(".poster-click").forEach(poster => {
            poster.addEventListener("click", async function () {
                sessionStorage.setItem("animeCode", poster.alt);
            });
        });
    }
<<<<<<< HEAD

    async searchByGenre(event) {
        const genre = event.target.dataset.genre;
        const newResponse = await this.client.getAnimeByGenre(genre, this.errorHandler);

        let items = "";

        console.log(newResponse[0]);
        for (let i = 0; i < newResponse.length; i++) {
            items += `
        <div class="poster">
          <a href="animepage.html"><img class="poster-click" alt=${newResponse[i].id} src=${newResponse[i].coverImage.large}></a>  
          <p>${newResponse[i].title.userPreferred}</p>
        </div>
      `;
        }

        const workArea = document.getElementById("work-area");
        workArea.innerHTML = items;

        this.genreButtons.forEach(button => button.classList.remove("active"));
        event.target.classList.add("active");


        document.querySelectorAll(".poster-click").forEach(poster => {
            poster.addEventListener("click", async function () {
                sessionStorage.setItem("animeCode", poster.alt);
            });
        });
    }

    animateKero() {
        const animationDuration = 7000;
        const startX = 0; // Initial X coordinate
        const startY = 0; // Initial Y coordinate
        const endX = 1500; // Final X coordinate
        const endY = 40; // Final Y coordinate

        // Apply the animation using CSS transitions
        this.keroElement.style.transition = `transform ${animationDuration}ms linear`;
        this.keroElement.style.transform = `translate(${endX}px, ${endY}px)`;

        // Reset the Kero's position after the animation ends
        setTimeout(() => {
            this.keroElement.style.transition = '';
            this.keroElement.style.transform = `translate(${startX}px, ${startY}px)`;
            this.animateKero(); // Repeat the animation
        }, animationDuration);
    }
}

const main = async () => {
=======
 
}





const main = async () =>{
>>>>>>> origin/juvisbranch
    const searchPage = new SearchPage();
    searchPage.mount();
    console.log("mount");
};

window.addEventListener('DOMContentLoaded', main);