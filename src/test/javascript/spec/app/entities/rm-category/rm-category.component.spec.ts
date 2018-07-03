/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ReqmanTestModule } from '../../../test.module';
import { RMCategoryComponent } from 'app/entities/rm-category/rm-category.component';
import { RMCategoryService } from 'app/entities/rm-category/rm-category.service';
import { RMCategory } from 'app/shared/model/rm-category.model';

describe('Component Tests', () => {
    describe('RMCategory Management Component', () => {
        let comp: RMCategoryComponent;
        let fixture: ComponentFixture<RMCategoryComponent>;
        let service: RMCategoryService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ReqmanTestModule],
                declarations: [RMCategoryComponent],
                providers: []
            })
                .overrideTemplate(RMCategoryComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(RMCategoryComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RMCategoryService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new RMCategory(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.rMCategories[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
