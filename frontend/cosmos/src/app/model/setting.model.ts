export class Setting
{
    public id: string;
    public name: string;
    public type: string;
    public regex: string;
    public value: string;
    public children: Setting[];
}