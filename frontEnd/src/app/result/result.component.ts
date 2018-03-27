import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import {AppService} from '../app.service';
import {ApiResult} from './api-result';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-api-result',
  templateUrl: './result.component.html',
  styleUrls: ['./result.component.css']
})
export class ResultComponent implements OnInit {

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private appService: AppService
  ) {
  }

  appResult: ApiResult;

  getJson() {
    this.appService.getMicrosoft()
      .subscribe(
        data => {
          this.appResult = data;
          console.log(this.appResult.api);
        },
        error => {
          console.log(error);
        }, () => {
          console.log('finis');
        });
  }
  goBack(): void {
    this.location.back();
  }

  ngOnInit() {
    this.getJson();
  }

}
