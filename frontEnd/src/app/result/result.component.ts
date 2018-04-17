import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import {AppService} from '../app.service';
import {ActivatedRoute} from '@angular/router';
import {Photos} from './model/photos';

@Component({
  selector: 'app-api-result',
  templateUrl: './result.component.html',
  styleUrls: ['./result.component.css']
})
export class ResultComponent implements OnInit {

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private appService: AppService,

  ) {
  }

  photosMicrosoft: Photos;

  photosAmazon: Photos;


  getMicrosoft() {
    this.appService.getMicrosoft()
      .subscribe( data => {
          console.log(data);
          this.photosMicrosoft = data;
        },
        error => {
          console.log(error);
        }, () => {
          console.log('finis Microsoft');
        });
  }

  getAmazon() {
    this.appService.getAmazon()
      .subscribe( data => {
          console.log(data);
          this.photosAmazon = data;
        },
        error => {
          console.log(error);
        }, () => {
          console.log('finis Amazon');
        });
  }


  goBack(): void {
    this.location.back();
  }

  init(): void {
    this.getMicrosoft();
    //this.getAmazon();
  }

  ngOnInit() {
    this.init();
  }

}
