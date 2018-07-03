import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IRMPage } from 'app/shared/model/rm-page.model';
import { RMPageService } from './rm-page.service';

@Component({
    selector: '-rm-page-update',
    templateUrl: './rm-page-update.component.html'
})
export class RMPageUpdateComponent implements OnInit {
    private _rMPage: IRMPage;
    isSaving: boolean;

    constructor(private rMPageService: RMPageService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ rMPage }) => {
            this.rMPage = rMPage;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.rMPage.id !== undefined) {
            this.subscribeToSaveResponse(this.rMPageService.update(this.rMPage));
        } else {
            this.subscribeToSaveResponse(this.rMPageService.create(this.rMPage));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IRMPage>>) {
        result.subscribe((res: HttpResponse<IRMPage>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get rMPage() {
        return this._rMPage;
    }

    set rMPage(rMPage: IRMPage) {
        this._rMPage = rMPage;
    }
}
