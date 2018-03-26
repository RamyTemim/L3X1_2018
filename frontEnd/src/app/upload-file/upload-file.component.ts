import { Component, OnInit } from '@angular/core';
import {AppService} from '../app.service';

@Component({
  selector: 'app-upload-file',
  templateUrl: './upload-file.component.html',
  styleUrls: ['./upload-file.component.css']
})
export class UploadFileComponent implements OnInit {
  selectedFilePhoto: File = null;
  selectedFileVideo: File = null;
  constructor(private appservice: AppService) { }

  ngOnInit() {
  }


  onSelectFilePhoto(eventa: any) {
    this.selectedFilePhoto = eventa.target.files;
  }
  onSelectFileVideo(eventb: any) {
    this.selectedFileVideo = eventb.target.files;
  }

  uploadFileTo() {
    this.appservice.storeFilePhoto(this.selectedFilePhoto).subscribe( res => {console.log(res); });
    this.appservice.storeFileVideo(this.selectedFileVideo).subscribe(res => {console.log(res);  });
  }

}
