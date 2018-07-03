/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ReqmanTestModule } from '../../../test.module';
import { RMTagComponent } from 'app/entities/rm-tag/rm-tag.component';
import { RMTagService } from 'app/entities/rm-tag/rm-tag.service';
import { RMTag } from 'app/shared/model/rm-tag.model';

describe('Component Tests', () => {
    describe('RMTag Management Component', () => {
        let comp: RMTagComponent;
        let fixture: ComponentFixture<RMTagComponent>;
        let service: RMTagService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ReqmanTestModule],
                declarations: [RMTagComponent],
                providers: []
            })
                .overrideTemplate(RMTagComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(RMTagComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RMTagService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new RMTag(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.rMTags[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
