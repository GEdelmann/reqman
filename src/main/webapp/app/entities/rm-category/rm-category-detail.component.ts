import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRMCategory } from 'app/shared/model/rm-category.model';

@Component({
    selector: '-rm-category-detail',
    templateUrl: './rm-category-detail.component.html'
})
export class RMCategoryDetailComponent implements OnInit {
    rMCategory: IRMCategory;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ rMCategory }) => {
            this.rMCategory = rMCategory;
        });
    }

    previousState() {
        window.history.back();
    }
}
