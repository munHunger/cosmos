import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HttpModule} from "@angular/http";

import { AppComponent } from './component/app/app.component';
import { NavbarComponent } from './component/navbar/navbar.component';
import { MovieListComponent } from './component/movieList/movieList.component';
import { NotFoundComponent } from './component/notFound/notFound.component';

import { MovieService } from './service/movies.service';

const appRoutes: Routes = [
  { path: "movies", component: MovieListComponent },
  { path: '',
    redirectTo: 'movies',
    pathMatch: 'full'
  },
  { path: '**', component: NotFoundComponent }
];

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    MovieListComponent,
    NotFoundComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(appRoutes),
    HttpModule
  ],
  providers: [ MovieService ],
  bootstrap: [AppComponent]
})
export class AppModule { }
