import { Rating } from './rating.model';

export class Movie
{
    public title: string;
    public year: number;
    public internal_id: string;
    public rating: Rating[];
    public genre: string[];
    public image_url: string;
}