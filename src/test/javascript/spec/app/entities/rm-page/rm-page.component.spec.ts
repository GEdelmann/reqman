/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ReqmanTestModule } from '../../../test.module';
import { RMPageComponent } from 'app/entities/rm-page/rm-page.component';
import { RMPageService } from 'app/entities/rm-page/rm-page.service';
import { RMPage } from 'app/shared/model/rm-page.model';

describe('Component Tests', () => {
    describe('RMPage Management Component', () => {
        let comp: RMPageComponent;
        let fixture: ComponentFixture<RMPageComponent>;
        let service: RMPageService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ReqmanTestModule],
                declarations: [RMPageComponent],
                providers: []
            })
                .overrideTemplate(RMPageComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(RMPageComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RMPageService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new RMPage(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.rMPages[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
