import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ApiResult} from './result/api-result';
import {Person} from './result/model/person';
import {Photos} from './result/model/photos';

@Injectable()
export class AppService {

  apiUrlMicrosoft = 'http://localhost:8080/microsoft/';
  apiUrlAmazon = 'http://localhost:8080/amazon/';
  apiUrlTest = 'http://localhost:8080/test/';
  urlPhoto = 'http://localhost:8080/photos';
  urlVideo = 'http://localhost:8080/videos';

  constructor(private http: HttpClient) { }

  getJson() {
    return this.http
      .get<Photos>(this.apiUrlTest);
  }

  getMicrosoft() {
    return this.http
      .get<Photos>(this.apiUrlMicrosoft);
  }

  getAmazon() {
    return this.http
      .get<Photos>(this.apiUrlAmazon);
  }

  storeFilePhoto (filePhoto: any) {
    const formDataPhoto: FormData = new FormData();
    filePhoto = new Blob(filePhoto);
    formDataPhoto.append('filePhoto', filePhoto, 'filePhoto');

    return this.http.post(this.urlPhoto , formDataPhoto);

  }


  storeFileVideo(fileVideo: any) {
    const formData: FormData = new FormData();
    fileVideo = new Blob(fileVideo);
    formData.append('fileVideo', fileVideo , 'fileVideo');
    return this.http.post(this.urlVideo , formData);
  }





}
