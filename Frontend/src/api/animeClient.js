import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ExampleClient from "../api/exampleClient";
import axios from "axios";

<<<<<<< HEAD
=======

>>>>>>> origin/new-branch-for-dev
/**
 * Logic needed for the view playlist page of the website.
 */
export default class AnimeClient extends BaseClass {

    constructor(props = {}) {
        super();
<<<<<<< HEAD
        const methodsToBind = ['getAnimeInfo'];
=======
        const methodsToBind = ['getAnimeInfo','getAnimeBySearch'];
>>>>>>> origin/new-branch-for-dev
        this.bindClassMethods(methodsToBind, this);
        this.props = props;
        this.clientLoaded(axios);
    }


    clientLoaded(client) {
        this.client = client;
        if (this.props.hasOwnProperty("onReady")) {
            this.props.onReady();
        }
    }

    async getAnimeInfo(id, errorCallback){
        try{
            const response = await this.client.get(`/anime/${id}`)
            return response.data;
        }catch(error){
<<<<<<< HEAD
            this.handleError("getExample", error, errorCallback)
        }
    }

    async getAnimeBySearch(title, errorCallback){
        let query = `
        query findPopularAnime($title: String){
    Page(page : 1, perPage : 50){
        media(search: $title type : ANIME){
            title{
            userPreferred
            }
            id
      description
      coverImage{
        large
      }
      startDate {
        year
        month
        day
      }
      season
      popularity
      averageScore
      episodes
      genres
        }
    }
}
`;
        let variables = {
            search : title
=======

        }
    }

    async getAnimeBySearch(title, errorCallback) {
        const map = new Map();
        let query = `
    query findPopularAnime($title: String) {
      Page(page: 1, perPage: 50) {
        media(search: $title, type: ANIME) {
          title {
            userPreferred
          }
          id
          description
          coverImage {
            large
          }
          startDate {
            year
            month
            day
          }
          season
          popularity
          averageScore
          episodes
          genres
        }
      }
    }
  `;
        let variables = {
            title: title
>>>>>>> origin/new-branch-for-dev
        };

        var url = 'https://graphql.anilist.co',
            options = {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json',
                },
                body: JSON.stringify({
                    query: query,
                    variables: variables
                })
            };

<<<<<<< HEAD
        fetch(url, options).then(handleResponse)
            .then(handleData)
            .catch(handleError);

=======
        try {
            const response = await fetch(url, options);
            const data = await handleResponse(response);
            return data.data.Page.media;
        } catch (error) {
            handleError(error);
        }
>>>>>>> origin/new-branch-for-dev
        function handleResponse(response) {
            return response.json().then(function (json) {
                return response.ok ? json : Promise.reject(json);
            });
        }
<<<<<<< HEAD

        function handleData(data) {
            console.log(data);
        }

=======
>>>>>>> origin/new-branch-for-dev
        function handleError(error) {
            alert('Error, check console');
            console.error(error);
        }

    }

<<<<<<< HEAD
    handleError(method, error, errorCallback) {
        console.error(method + " failed - " + error);
        if (error.response.data.message !== undefined) {
            console.error(error.response.data.message);
        }
        if (errorCallback) {
            errorCallback(method + " failed - " + error);
        }
    }
=======
>>>>>>> origin/new-branch-for-dev
}
    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
