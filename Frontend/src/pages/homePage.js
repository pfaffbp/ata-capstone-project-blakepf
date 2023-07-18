import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import HomePageClient from "../api/homePageClient";
const map = new Map;

class HomePage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['renderHomePage', 'onLoad', 'getNotifications'], this);
        this.dataStore = new DataStore();
    }

    async mount() {
        this.dataStore.addChangeListener(this.renderHomePage)
        document.getElementById('logout').addEventListener('click', this.Logout);
        this.client = new HomePageClient();
        this.renderHomePage();
        this.onLoad();
    }

    async onLoad() {
        let user = localStorage.getItem('displayName')

        let LoggedInArea = document.getElementById('userLoggedIn');
        if (user != null) {
            document.getElementById("bell").classList.remove("hide");
            document.getElementById("bell").addEventListener("click", this.getNotifications);

            LoggedInArea.innerHTML = user;
        } else
            LoggedInArea.innerHTML = "Login";

        const response = await this.client.getNotifications(user, this.errorHandler());
        console.log(response);
    }

    async Logout(event) {
        event.preventDefault();
        localStorage.clear();
        window.location.href = "login.html";
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

        // (Popular) On load, the page renders the first 6 items of the list.

        for (let i = 0; i < 6; i++) {
            console.log("Inside here")

            items += `
            <div class="anime-poster-class">
                <div class="anime-poster">
                <a href="animePage.html"><img class = "poster-click" alt=${response[i].animeId} src=${response[i].image}></a>
                        <h5>${response[i].title}</h5>
                </div>
            </div>
            `;

        }

        workArea.innerHTML = items;

        // (Popular) When you click on the right arrow, loads the next 6 items using this function.

        document.getElementById("next-1").addEventListener("click", function loadNext1() {
            items = "";
            for (let i = 5; i < 11; i++) {
                console.log("Inside here")

                items += `
               <div class="anime-poster-class">
               <div class="anime-poster">
               <a href="animePage.html"><img class = "poster-click" alt=${response[i].animeId} src=${response[i].image}></a>
                       <h5>${response[i].title}</h5>
               </div>
           </div>
               `;

            }

            workArea.innerHTML = items;
        }
        )

        // (Popular) When you click on the left arrow, loads the original 6 items using this function.

        document.getElementById("prev-1").addEventListener("click", function loadPrev1() {
            items = "";
            for (let i = 0; i < 6; i++) {
                console.log("Inside here")
                items += `
               <div class="anime-poster-class">
               <div class="anime-poster">
               <a href="animePage.html"><img class = "poster-click" alt=${response[i].animeId} src=${response[i].image}></a>
                       <h5>${response[i].title}</h5>
               </div>
           </div>
               `;
            }

            workArea.innerHTML = items;
        }
        )


        // (Seasonal) On load, the page renders the first 6 items of the list.

        for (let i = 10; i < 16; i++) {
            console.log("Inside here")

            seasonalItems += `
            <div class="anime-poster-class">
            <div class="anime-poster">
            <a href="animePage.html"><img class = "poster-click" alt=${response[i].animeId} src=${response[i].image}></a>
                    <h5>${response[i].title}</h5>
            </div>
        </div>
            `;
        }

        seasonAnime.innerHTML = seasonalItems;

        // (Seasonal) When you click on the right arrow, loads the next 6 items using this function.

        document.getElementById("next-2").addEventListener("click", function loadNext2() {
            seasonalItems = "";
            for (let i = 16; i < 22; i++) {
                console.log("Inside here")

                seasonalItems += `
               <div class="anime-poster-class">
                <div class="anime-poster">
                <a href="animePage.html"><img class = "poster-click" alt=${response[i].animeId} src=${response[i].image}></a>
                        <h5>${response[i].title}</h5>
                </div>
            </div>
               `;

            }

            seasonAnime.innerHTML = seasonalItems;
        }
        )

        // (Popular) When you click on the left arrow, loads the original 6 items using this function.

        document.getElementById("prev-2").addEventListener("click", function loadPrev2() {
            seasonalItems = "";
            for (let i = 10; i < 16; i++) {
                console.log("Inside here")

                seasonalItems += `
               <div class="anime-poster-class">
               <div class="anime-poster">
               <a href="animePage.html"><img class = "poster-click" alt=${response[i].animeId} src=${response[i].image}></a>
                       <h5>${response[i].title}</h5>
               </div>
           </div>
               `;
            }
            seasonAnime.innerHTML = seasonalItems;
        }
        )


        // (Highly Rated) On load, the page renders the first 6 items of the list.

        for (let i = 22; i < 28; i++) {
            console.log("Inside here")

            highRatedItems += `
            <div class="anime-poster-class">
            <div class="anime-poster">
            <a href="animePage.html"><img class = "poster-click" alt=${response[i].animeId} src=${response[i].image}></a>
                    <h5>${response[i].title}</h5>
            </div>
        </div>
            `;
        }

        highlyRatedAnime.innerHTML = highRatedItems;

        // (Highly Rated) When you click on the right arrow, loads the next 6 items using this function.

        document.getElementById("next-3").addEventListener("click", function loadNext2() {
            highRatedItems = "";
            for (let i = 28; i < 34; i++) {
                console.log("Inside here")

                highRatedItems += `
               <div class="anime-poster-class">
               <div class="anime-poster">
               <a href="animePage.html"><img class = "poster-click" alt=${response[i].animeId} src=${response[i].image}></a>
                       <h5>${response[i].title}</h5>
               </div>
           </div>
               `;

            }

            highlyRatedAnime.innerHTML = highRatedItems;
        }
        )

        // (Highly Rated) When you click on the left arrow, loads the original 6 items using this function.

        document.getElementById("prev-3").addEventListener("click", function loadPrev2() {
            highRatedItems = "";
            for (let i = 22; i < 28; i++) {
                console.log("Inside here")

                highRatedItems += `
               <div class="anime-poster-class">
               <div class="anime-poster">
               <a href="animePage.html"><img class = "poster-click" alt=${response[i].animeIdw} src=${response[i].image}></a>
                       <h5>${response[i].title}</h5>
               </div>
           </div>
               `;
            }

            highlyRatedAnime.innerHTML = highRatedItems;
        }
        )

        let docs = document.querySelectorAll('.poster-click');

        docs.forEach(href => {
            href.addEventListener('click', async function () {
                sessionStorage.setItem("animeCode", href.alt);
            });
        });
        await console.log(sessionStorage.getItem("animeCode")); // stores the animeId to get data;
        console.log(docs);
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
    const homePage = new HomePage();
    homePage.mount();
    console.log("mount");
};

window.addEventListener('DOMContentLoaded', main);