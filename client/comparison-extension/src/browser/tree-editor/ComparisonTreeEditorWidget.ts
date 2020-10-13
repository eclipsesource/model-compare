import { injectable, inject } from 'inversify';
import { ILogger } from '@theia/core';
//import { ModelServerClient } from '@eclipse-emfcloud/modelserver-theia/lib/common';
//import { ModelServerSubscriptionService } from '@eclipse-emfcloud/modelserver-theia/lib/browser';
import { WorkspaceService } from '@theia/workspace/lib/browser/workspace-service';
import { TreeEditor } from '../tree-widget/interfaces';
//import { NavigatableTreeEditorOptions } from '../tree-widget/navigatable-tree-editor-widget';
import { AddCommandProperty, MasterTreeWidget } from '../tree-widget/master-tree-widget';
import { TreeNode, CompositeTreeNode, Title, Widget, Saveable, WidgetManager, OpenViewArguments, ApplicationShell } from '@theia/core/lib/browser';
import { ComparisonBackendService } from '../../common/protocol';
import { BaseTreeEditorWidget } from '../tree-widget';
import { ComparisonExtensionConfiguration } from '../comparison-extension-configuration';
import { GraphicalComparisonOpener } from '../graphical/graphical-comparison-opener';
import URI from '@theia/core/lib/common/uri';
import { GraphicalComparisonWidget, GraphicalComparisonWidgetOptions } from '../graphical/graphical-comparison-widget';


export const ComparisonTreeEditorWidgetOptions = Symbol(
  'ComparisonTreeEditorWidgetOptions'
);
export interface ComparisonTreeEditorWidgetOptions {
  left: string,
  right: string,
  origin: string,
  merges: Array<MergeInstruction>,
  conflicts: Array<String>
}

export interface MergeInstruction {
  type: string,
  target: string,
  direction: string
}


@injectable()
export class ComparisonTreeEditorWidget extends BaseTreeEditorWidget implements Saveable{

  protected options: ComparisonTreeEditorWidgetOptions;
  protected comparisonResponse: JSONCompareResponse;

  constructor(
    @inject(ComparisonBackendService) readonly comparisonBackendService: ComparisonBackendService,
    @inject(ComparisonExtensionConfiguration) readonly config: ComparisonExtensionConfiguration,
    @inject(GraphicalComparisonOpener) protected readonly graphicalOpener: GraphicalComparisonOpener,
    @inject(WidgetManager) protected readonly widgetManager: WidgetManager,
    @inject(ApplicationShell) protected readonly shell: ApplicationShell,
    @inject(MasterTreeWidget)
    readonly myTreeWidgetOverview: MasterTreeWidget,
    @inject(MasterTreeWidget)
    readonly myTreeWidgetModel1: MasterTreeWidget,
    @inject(MasterTreeWidget)
    readonly myTreeWidgetModel2: MasterTreeWidget,
    @inject(WorkspaceService)
    readonly workspaceService: WorkspaceService,
    @inject(ILogger) readonly logger: ILogger
    //@inject(ModelServerClient)
    //private readonly modelServerApi: ModelServerClient
    //@inject(ModelServerSubscriptionService)
    //private readonly subscriptionService: ModelServerSubscriptionService
  ) {
    super(
      myTreeWidgetOverview,
      myTreeWidgetModel1,
      myTreeWidgetModel2,
      workspaceService,
      logger,
      ComparisonTreeEditorWidget.WIDGET_ID
    );

    //console.log("path: " + this.getModelIDToRequest());
    this.showInformation("loading...", "gray");

    window.onbeforeunload = () => this.dispose();
  }

  public setContent(options) {
    this.options = options;
    this.comparisonBackendService.getNewComparison(options.left, options.right, options.origin, this.mergesToString()).then(r => {
      let response: JSONCompareResponse = JSON.parse(r);
      this.comparisonResponse = response;

      if (response.error.trim() !== "") {
        this.logger.error(response.error);
        this.showInformation(response.error, "red", "fas fa-exclamation-triangle red");
        return;
      }
      this.instanceData = response.overviewTree;
      this.myTreeWidgetModel1.treeTitle = "Left: " + this.options.left.replace(/^.*[\\\/]/, '');
      this.myTreeWidgetModel2.treeTitle = "Right: " + this.options.right.replace(/^.*[\\\/]/, '');

      this.myTreeWidgetOverview.treeTitle = "Differences overview:";
      this.myTreeWidgetOverview
        .setData({ error: false, data: this.instanceData})
        .then(() => this.myTreeWidgetOverview.selectFirst());

      this.myTreeWidgetModel1
        .setData({ error: false, data: response.leftTree})
        .then(() => this.myTreeWidgetModel1.selectFirst());

      this.myTreeWidgetModel2
        .setData({ error: false, data: response.rightTree })
        .then(() => this.myTreeWidgetModel2.selectFirst());

      this.myTreeWidgetOverview.model.refresh();

      this.actionWidget.setGraphicalComparisonVisibility(this.config.supportGraphicalComparison());
    });
  }

  /**
   *  Returns the opened file
   
  private getModelIDToRequest(): string {
    const rootUriLength = this.workspaceService
      .getWorkspaceRootUri(this.options.uri)
      .toString().length;
    return this.options.uri.toString().substring(rootUriLength + 1);
  }
  */

  protected treeSelectionChanged(treeWidget: MasterTreeWidget, selectedNodes: readonly Readonly<TreeEditor.Node>[]): void {
    if (treeWidget === this.treeWidgetOverview) {
      if (selectedNodes.length !== 0) {
        this.selectedNode = selectedNodes[0];
        this.navigateToSelection(this.treeWidgetModel1, this.selectedNode.jsonforms.data.uuid);
        this.navigateToSelection(this.treeWidgetModel2, this.selectedNode.jsonforms.data.uuid);
        this.actionWidget.updateActivation(this.selectedNode.jsonforms.data.type);        
      }
    }
    this.update();
  }

  private navigateToSelection(treeWidget: MasterTreeWidget, uuid: string): void{
    if (!this.selectedNode) {
      return;
    }

    let rootNode = treeWidget.model.root as TreeEditor.RootNode;
    let nodes: Array<TreeNode> = [...rootNode.children];
    treeWidget.model.collapseAll(rootNode).then(() => {
      while (nodes.length !== 0) {
        const node : TreeNode = nodes.pop();
        if (TreeEditor.Node.is(node)) {
          if (node.jsonforms.data.uuid === uuid) {
            let parent: CompositeTreeNode = node.parent;
            while(parent && parent !== rootNode) {
              if (TreeEditor.Node.is(parent)) {
                treeWidget.model.expandNode(parent);
              }
              parent = parent.parent;
            }
            treeWidget.model.selectNode(node);
            return;
          }
          node.children.forEach(element => {
            nodes.push(element);
          });
        }
      }

      // try if there are connectedd uuids
      let relatedUuid = this.comparisonResponse.uuidConnection[uuid];
      if (relatedUuid) {
        this.navigateToSelection(treeWidget, relatedUuid);
      }
    });
  }


  protected deleteNode(node: TreeEditor.Node): void {
    throw new Error("Method not implemented.");
  }
  protected addNode({ node, type, property }: AddCommandProperty): void {
    throw new Error("Method not implemented.");
  }
  protected handleFormUpdate(data: any, node: TreeEditor.Node): void {
    throw new Error("Method not implemented.");
  }

  public save(): void {
    if (this.options) {
      this.comparisonBackendService.merge(this.options.left, this.options.right, this.options.origin, this.mergesToString(), "").then(response => {
        //alert(response);
        this.options.merges = [];
        this.setDirty(false);
        this.setContent(this.options);
      });
    }
  }

  show(): void {
    console.log("show");
    super.show();
    /*
    if (this.delayedRefresh) {
      this.delayedRefresh = false;
      this.treeWidget.model.refresh();
    }
    */
  }

  private showInformation(text: string, color: string, icon: string = "") {
    this.instanceData = {
      eClass: "information",
      name: text,
      icon: icon,
      color: color,
      uuid: "",
      children: []
    }

    this.myTreeWidgetOverview
      .setData({ error: false, data: this.instanceData})
      .then(() => this.myTreeWidgetOverview.selectFirst());
  }

  protected configureTitle(title: Title<Widget>): void {
    title.label = "Comparison Tree View";
    title.caption = ComparisonTreeEditorWidget.WIDGET_LABEL;
    title.closable = true;
    title.iconClass = 'fas fa-columns file-icon';
  }

  private addMerge(type: string, target: string, direction: string): void {
    const merge: MergeInstruction = {type: type, target: target, direction: direction};
    this.options.merges.push(merge);
  }

  private mergesToString(): string {
    let merges = [];
    this.options.merges.forEach(merge => merges.push(merge.type + ";" + merge.target + ";" + merge.direction));
    const merg = merges.join();
    return merg;
  }

  public merge(toLeft: boolean, all: boolean, conflict: boolean): void {
    this.setDirty(true);
    const type = (conflict) ? "conflict" : "diff";
    const direction = (toLeft) ? "left" : "right";
    let target;
    if (all) {
      target = "all";
    } else {
      if (this.selectedNode) {
        target = this.selectedNode.jsonforms.data.uuid;
      }
    }

    this.addMerge(type, target, direction);
    this.setContent(this.options);
  }

  public undoMerge(): void {
    this.options.merges.pop();
    if (this.options.merges.length === 0) {
      this.setDirty(false);
    }
    this.setContent(this.options);
    
  }

  public showGraphicalComparison(): void {
    if (this.dirty) {

    }

    this.options.left

    this.graphicalOpener.getHighlights(this.options.left, this.options.right).then(async (highlights: any) => {
      const leftWidget = await this.graphicalOpener.getLeftDiagram(new URI(this.options.left), highlights);
      const rightWidget = await this.graphicalOpener.getRightDiagram(new URI(this.options.right), highlights);
      const options: GraphicalComparisonWidgetOptions = {
          left: leftWidget,
          right: rightWidget
      };
      this.widgetManager.getOrCreateWidget(GraphicalComparisonWidget.WIDGET_ID).then(widget => {
        (<GraphicalComparisonWidget> widget).setContent(options);
        this.openView(widget, {activate: true});
      })
    });
  }

  async openView(widget: Widget, args: Partial<OpenViewArguments> = {}): Promise<Widget> {
    const shell = this.shell;
    const tabBar = shell.getTabBarFor(widget);
    const area = shell.getAreaFor(widget);
    if (!tabBar) {
        const widgetArgs: OpenViewArguments = {
            reveal: true,
            ...args
        };
        await shell.addWidget(widget, widgetArgs);
    } else if (args.toggle && area && shell.isExpanded(area) && tabBar.currentTitle === widget.title) {
        switch (area) {
            case 'left':
            case 'right':
                await shell.collapsePanel(area);
                break;
            case 'bottom':
                if (shell.bottomAreaTabBars.length === 1) {
                    await shell.collapsePanel('bottom');
                }
                break;
            default:
                await this.shell.closeWidget(GraphicalComparisonWidget.WIDGET_ID);
        }
        return widget;
    }
    if (widget.isAttached && args.activate) {
        await shell.activateWidget(GraphicalComparisonWidget.WIDGET_ID);
    } else if (widget.isAttached && args.reveal) {
        await shell.revealWidget(GraphicalComparisonWidget.WIDGET_ID);
    }
    return widget;
  }

}

export namespace ComparisonTreeEditorWidget {
  export const WIDGET_ID = 'json-forms-comparison-tree-editor';
  export const EDITOR_ID = 'com.eclipsesource.comparison.editor';
}
