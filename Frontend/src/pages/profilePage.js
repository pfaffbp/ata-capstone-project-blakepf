
import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ProfileClient from "../api/profileClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class ProfilePage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onLoad', 'renderUserProfile'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {

        this.client = new ProfileClient();
        document.getElementById('logout').addEventListener('click', this.Logout);
        this.dataStore.addChangeListener(this.renderUserProfile)
        this.onLoad();
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
            /*    for (let anime of uData){
                  animeList +=`
                  ${anime.favoriteAnime}
                  `;
              }*/
            let friendList =""
            /*    for (let anime of uData){
                  animeList +=`
                  ${anime.favoriteAnime}
                  `;
              }*/


            displayName.innerHTML = items;
            ageArea.innerHTML = age;
            nameArea.innerHTML = name;
            bioArea.innerHTML = bio;

        } else {
            displayName.innerHTML = "Display Name:";
            ageArea.innerHTML = "Age:";
            nameArea.innerHTML = "Name:";
            bioArea.innerHTML = "Bio:";


        }
    }

    // Event Handlers --------------------------------------------------------------------------------------------------


    async onLoad(){
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
    const profilePage = new ProfilePage();
    profilePage.mount();
};

window.addEventListener('DOMContentLoaded', main);
