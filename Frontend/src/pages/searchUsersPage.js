
import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import SearchUserClient from "../api/searchUsersClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class SearchUsersPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods([ 'renderUserProfile', 'onSearch'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {

        this.client = new SearchUserClient();
        this.dataStore.addChangeListener(this.renderUserProfile)
        document.getElementById('logout').addEventListener('click', this.Logout);
        document.getElementById('searchButton').addEventListener('click', this.onSearch);

    }

    // Render Methods --------------------------------------------------------------------------------------------------

    async renderUserProfile() {
        let nameArea = document.getElementById("fullName");
        let displayName = document.getElementById("displayName");
        let ageArea = document.getElementById("age");
        let bioArea = document.getElementById('bio');
        let animeArea = document.getElementById('favAnime')
        let friendArea = document.getElementById('friendsList')

        const uData = this.dataStore.get("userData");
        if (uData) {
            let items ="";
            items += `
                   ${uData.displayName}                            
                `;
            let age ="";
            age += `
                   Age: ${uData.age}                            
                `;
            let name ="";
            name += `
                    Name: ${uData.fullName}                             
                `;
            let bio ="";
            bio += `
                    Bio: ${uData.bio}                           
                `;
            let animeList =""
                  animeList +=`
                 Name: ${uData.favoriteAnime}
                  `;

            let followers =""
                  followers +=`
                  Name: ${uData.followers}
                  `;



            displayName.innerHTML = items;
            ageArea.innerHTML = age;
            nameArea.innerHTML = name;
            bioArea.innerHTML = bio;
            animeArea.innerHTML = animeList;
            friendArea.innerHTML = followers;


        } else {
            displayName.innerHTML = "Display Name:";
            ageArea.innerHTML = "Age:";
            nameArea.innerHTML = "Name:";
            bioArea.innerHTML = "Bio:";

        }
    }

    // Event Handlers --------------------------------------------------------------------------------------------------


    async onSearch(event){
        event.preventDefault();
        let result = await this.client.getUserData(this.errorHandler);
        this.dataStore.set("userData", result);
    }

    async Logout(event){
        event.preventDefault();
        localStorage.clear();
        window.location.href = "login.html";
    }
}



/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const searchUsersPage = new SearchUsersPage();
    searchUsersPage.mount();
};

window.addEventListener('DOMContentLoaded', main);