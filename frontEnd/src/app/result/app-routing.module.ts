import {RouterModule, Routes} from '@angular/router';
import {UploadFileComponent} from '../upload-file/upload-file.component';
import {ResultComponent} from './result.component';
import {NgModule} from '@angular/core';

const routes: Routes = [
  { path: '', redirectTo: '/inputFile', pathMatch: 'full' },
  { path: 'inputFile', component: UploadFileComponent},
  { path: 'results', component: ResultComponent}
];

@NgModule({
  imports: [
    // Initialise un router avec le contenu de routes
    RouterModule.forRoot(routes)
  ],
  exports : [
    RouterModule
  ]

})
export class AppRoutingModule {

}
