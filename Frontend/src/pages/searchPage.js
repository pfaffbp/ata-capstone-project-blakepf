import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import HomePageClient from "../api/homePageClient";
import AnimeClient from "../api/animeClient";

class SearchPage extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['renderSearchPage', 'searchByGenre', 'animateKero', 'onLoad', 'getNotifications'], this);
        this.dataStore = new DataStore();
        this.genreButtons = Array.from(document.querySelectorAll('.button-86'));
        this.keroElement = document.getElementById('kero');
        this.animateKero = this.animateKero.bind(this);
        this.animateKero();

    }

    async mount() {
        document.getElementById("search-form").addEventListener("submit", this.renderSearchPage);
        this.genreButtons.forEach(button => button.addEventListener("click", this.searchByGenre));
        this.keroElement.addEventListener("animationstart", this.animateKero);
        this.client = new AnimeClient();
        document.getElementById('logout').addEventListener('click', this.Logout);
        this.onLoad();
    }

    async renderSearchPage(event) {
        event.preventDefault()

        let workArea = document.getElementById("work-area");
        let searchValue = document.getElementById("search-bar").value;
        let searchValueIntoString = `${searchValue}`;

        console.log(searchValue);

        const newResponse = await this.client.getAnimeBySearch(searchValueIntoString, this.errorHandler);
        document.getElementById("genre-form").addEventListener("submit", this.renderSearchPage);
        this.client = new AnimeClient();
    }

    async renderSearchPage(event) {
        event.preventDefault();
        let workArea = document.getElementById("work-area");
        let searchValue = document.getElementById("search-bar").value;
        let searchValueIntoString = `${searchValue}`;

        console.log(searchValue);

        const newResponse = await this.client.getAnimeBySearch(searchValueIntoString, this.errorHandler);


        let items = "";

        console.log(newResponse[0]);
        for (let i = 0; i < newResponse.length; i++) {
            items += `
        <div class="poster">
          <a href="animePage.html"><img class="poster-click" alt=${newResponse[i].id} src=${newResponse[i].coverImage.large}></a>  
          <p>${newResponse[i].title.userPreferred}</p>
        </div>
      `;
        }

        const style = document.createElement('style');
        style.innerHTML = ``;

        workArea.innerHTML = items;

        document.querySelectorAll(".poster-click").forEach(poster => {
            poster.addEventListener("click", async function () {
                sessionStorage.setItem("animeCode", poster.alt);
            });
        });
    }

    async searchByGenre(event) {
        const genre = event.target.dataset.genre;
        const newResponse = await this.client.getAnimeByGenre(genre, this.errorHandler);

        let items = "";

        console.log(newResponse[0]);
        for (let i = 0; i < newResponse.length; i++) {
            items += `
        <div class="poster">
          <a href="animePage.html"><img class="poster-click" alt=${newResponse[i].id} src=${newResponse[i].coverImage.large}></a>  
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
        const endX = 800; // Final X coordinate
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

    async Logout(event) {
        event.preventDefault();
        localStorage.clear();
        window.location.href = "login.html";
    }
    async onLoad() {
        let user = localStorage.getItem('displayName')

        let LoggedInArea = document.getElementById('userLoggedIn');
        if (user != null) {
            document.getElementById("bell").addEventListener("click", this.getNotifications);

            document.getElementById("bell").classList.remove("hide");
            LoggedInArea.innerHTML = user;
        } else
            LoggedInArea.innerHTML = "Login";

        const response = await this.client.getNotifications(user, this.errorHandler());
        console.log(response);
    }


    async getNotifications() {
        console.log("In getNotifications");                                 // Checks to see if it makes it to this method when clicking the bell.
        let user = localStorage.getItem("displayName");                     // Grabbing the user name. 
        console.log(user);                                                  // Logging the user name to check and see if it pulls the correct one. 

        const response = await this.client.getNotifications(localStorage.getItem("displayName"), this.errorHandler)         // Sends a get request to the Lambda API.

        let notificationDiv = document.getElementById("notification-items");     // Create variable to point to notification dropdown.
        let notificationHtml = "";                                               // Create variable of an empty string.

        for (let i = 0; i < response.length; i++) {
            console.log("In fori loop to gather response details");
            console.log(response.length)                                         // Verify length
            notificationHtml +=
                `<div class = "notification"><img src = "https://i.pinimg.com/736x/f8/84/7b/f8847b5a92b0e321d6df26ebaee9b39c.jpg" class = "notification-img"> 
            <p> ${response[i].userRequest} </p>
        </div> `
            console.log(response[i].userRequest)
        }

        notificationDiv.innerHTML = notificationHtml;                              // Setting the newly created html to the innerHtml of the notification dropdown.
        console.log(notificationDiv.innerHTML)
        console.log(response);
    }

}

const main = async () => {
    const searchPage = new SearchPage();
    searchPage.mount();
    console.log("mount");
};

window.addEventListener('DOMContentLoaded', main);