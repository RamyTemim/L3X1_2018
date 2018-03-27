import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';


import { AppComponent } from './app.component';
import { UploadFileComponent } from './upload-file/upload-file.component';
import { ResultComponent } from './result/result.component';
import {HttpClientModule} from '@angular/common/http';
import {AppService} from './app.service';
import {AppRoutingModule} from './result/app-routing.module';


@NgModule({
  declarations: [
    AppComponent,
    UploadFileComponent,
    ResultComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule
  ],
  providers: [
    AppService
  ],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule { }
