import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRMTag } from 'app/shared/model/rm-tag.model';

@Component({
    selector: '-rm-tag-detail',
    templateUrl: './rm-tag-detail.component.html'
})
export class RMTagDetailComponent implements OnInit {
    rMTag: IRMTag;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ rMTag }) => {
            this.rMTag = rMTag;
        });
    }

    previousState() {
        window.history.back();
    }
}
