import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRMPage } from 'app/shared/model/rm-page.model';

@Component({
    selector: '-rm-page-detail',
    templateUrl: './rm-page-detail.component.html'
})
export class RMPageDetailComponent implements OnInit {
    rMPage: IRMPage;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ rMPage }) => {
            this.rMPage = rMPage;
        });
    }

    previousState() {
        window.history.back();
    }
}
