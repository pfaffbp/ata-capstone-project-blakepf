import BaseClass from "../util/baseClass";
import axios from 'axios'

export default class HomePageClient extends BaseClass{

    constructor(props = {}) {
        super();
        const methodsToBind = ['getFrontPage'];
        this.bindClassMethods(methodsToBind, this);
        this.props = props;
        this.clientLoaded(axios);
    }
    /**
     * Run any functions that are supposed to be called once the client has loaded successfully.
     * @param client The client that has been successfully loaded.
     */
    clientLoaded(client){
        this.client = client;
        if(this.props.hasOwnProperty("onReady")){
            this.props.onReady();
        }
    }


    async getFrontPage(errorCallBack){
        console.log("GetFrontPage");
        try {
            const response = await this.client.get(`anime/homePage/`);
            // console.log(response.data);
            return response.data;
        }catch(error){
            this.handleError("getFrontPage", error, errorCallBack)
        }
    }



    handleError(method, error, errorCallback) {
        console.error(method + " failed - " + error);
        if (error.response.data.message !== undefined) {
            console.error(error.response.data.message);
        }
        if (errorCallback) {
            errorCallback(method + " failed - " + error);
        }
    }

}