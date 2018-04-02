import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import {AppService} from '../app.service';
import {ActivatedRoute} from '@angular/router';
import {Person} from './model/person';
import {TypedJSON} from '@upe/typedjson';
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
    private appService: AppService
  ) {
  }

  photos: Photos;
  person: Person;

  getJson() {
    this.appService.getMicrosoft()
      .subscribe( data => {
          console.log(data);
          this.photos = data;
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
