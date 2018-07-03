import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IRMTag } from 'app/shared/model/rm-tag.model';
import { Principal } from 'app/core';
import { RMTagService } from './rm-tag.service';

@Component({
    selector: '-rm-tag',
    templateUrl: './rm-tag.component.html'
})
export class RMTagComponent implements OnInit, OnDestroy {
    rMTags: IRMTag[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private rMTagService: RMTagService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.rMTagService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<IRMTag[]>) => (this.rMTags = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.rMTagService.query().subscribe(
            (res: HttpResponse<IRMTag[]>) => {
                this.rMTags = res.body;
                this.currentSearch = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInRMTags();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IRMTag) {
        return item.id;
    }

    registerChangeInRMTags() {
        this.eventSubscriber = this.eventManager.subscribe('rMTagListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
