import { Component, Input } from '@angular/core';
import { MovieService } from '../../service/movies.service';
import { Movie } from '../../model/movie.model';

@Component({
  selector: 'movie',
  templateUrl: './movie.component.html',
  styleUrls: ['./movie.component.css']
})
export class MovieComponent {
    @Input()
    private movie: Movie;

    private selected: boolean = false;

    private loaded: boolean = false;

    private hover: boolean = false;

    constructor(private movieService: MovieService)
    {
    }

    public select()
    {
        this.selected = true;
        if(!this.loaded)
            this.movieService.getExtendMovie(this.movie).subscribe(res => 
                {
                    this.movie = res;
                    this.loaded = true;
                });
    }

    public deselect($event)
    {
        this.selected = false;
        $event.stopPropagation();
    }

    public calcRating(): number
    {
        let r = 0;
        let c = 0;
        this.movie.rating.forEach(rating => {
            r += rating.rating/rating.max;
            c++;
        });
        var num = (r/c)*10;
        num /= 2;
        return Math.round(num);
    }

    public asArray(num: number): number[]
    {
        let result: number[] = [];
        for(let i = 0; i < num; i++)
            result.push(i);
        return result;
    }
}
