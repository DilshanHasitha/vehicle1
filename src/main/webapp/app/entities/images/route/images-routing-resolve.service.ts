import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IImages } from '../images.model';
import { ImagesService } from '../service/images.service';

@Injectable({ providedIn: 'root' })
export class ImagesRoutingResolveService implements Resolve<IImages | null> {
  constructor(protected service: ImagesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IImages | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((images: HttpResponse<IImages>) => {
          if (images.body) {
            return of(images.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
