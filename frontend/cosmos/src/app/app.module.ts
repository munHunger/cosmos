import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {HttpModule} from "@angular/http";

import { AppComponent } from './component/app/app.component';
import { NavbarComponent } from './component/navbar/navbar.component';
import { MovieListComponent } from './component/movieList/movieList.component';
import { NotFoundComponent } from './component/notFound/notFound.component';
import { MovieComponent } from './component/movie/movie.component';
import { SettingsComponent } from './component/settings/settings.component';

import { MovieService } from './service/movies.service';
import { SettingsService } from './service/settings.service';

const appRoutes: Routes = [
  { path: "movies", component: MovieListComponent },
  { path: "settings", component:SettingsComponent },
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
    NotFoundComponent,
    MovieComponent,
    SettingsComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(appRoutes),
    HttpModule,
    FormsModule
  ],
  providers: [ MovieService, SettingsService ],
  bootstrap: [AppComponent]
})
export class AppModule { }
